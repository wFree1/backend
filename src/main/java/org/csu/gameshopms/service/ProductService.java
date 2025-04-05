package org.csu.gameshopms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.micrometer.common.util.StringUtils;
import org.csu.gameshopms.entity.Comment;
import org.csu.gameshopms.entity.Edition;
import org.csu.gameshopms.entity.Product;
import org.csu.gameshopms.mapper.CommentMapper;
import org.csu.gameshopms.mapper.EditionMapper;
import org.csu.gameshopms.mapper.ProductMapper;
import org.csu.gameshopms.vo.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private EditionMapper editionMapper;

    @Value("${image.upload-dir}")
    private String uploadDir; // 从配置文件中注入路径

    public Product getProductDetail(int productId) {
        Product product=productMapper.selectById(productId);
        // 2. 获取关联评论
        if (product != null) {
            List<Comment> comments = commentMapper.selectByProductId(productId);
            product.setComments(comments);
        }
        // 获取版本信息
        if (product != null){
        List<Edition> editions = editionMapper.selectList(
                new QueryWrapper<Edition>().eq("product_id", productId)
        );
        product.setEditions(editions);
        }
        return product;
    }

    public List<Product> getProductList() {
        return List.of();
    }

    public List<Product> getAllProducts() {
        List<Product> products = productMapper.selectList(null);

        // 打印 List<Product> 的字符串形式
        System.out.println("Products: " + products); // 自动调用每个元素的 toString()

        return products;
    }


    private List<ProductVO> convertToVOList(List<Product> products) {
        return products.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    // 实体转VO方法
    private ProductVO convertToVO(Product product) {
        if (product == null) return null;

        ProductVO vo = new ProductVO();
        BeanUtils.copyProperties(product, vo);
        return vo;
    }
    /**
     * 添加单个商品
     *
     * @param product     商品实体（不含图片路径）
     * @param pictureFile 图片文件（允许为空）
     */
    @Transactional
    public void addProduct(Product product, MultipartFile pictureFile) {
        System.out.println("接收到的版本信息: " + product.getEditions()); // 添加日志
        // 1. 基础校验
        validateProduct(product);

        // 2. 处理图片上传
        if (pictureFile != null && !pictureFile.isEmpty()) {
            String picturePath = uploadImage(pictureFile);
            product.setPicture(picturePath);
        }

        // 3. 插入数据库
        productMapper.insert(product);
        int productId = product.getId(); // 获取自增的 productId
        // 4. 保存版本信息
        if (product.getEditions() != null && !product.getEditions().isEmpty()) {
            for (Edition edition : product.getEditions()) {
                edition.setProductId(productId); // 绑定 productId
            }
            editionMapper.insertBatch(product.getEditions()); // 批量插入（需 EditionMapper 支持）
        }
    }

    private void validateProduct(Product product) {
        if (StringUtils.isBlank(product.getName())) {
            throw new IllegalArgumentException("商品名称不能为空");
        }
        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("价格必须大于0");
        }
        if (StringUtils.isBlank(product.getCategory())) {
            throw new IllegalArgumentException("商品分类不能为空");
        }
    }

    private String uploadImage(MultipartFile file) {
        try {
            //生成安全文件名
            String originalName = file.getOriginalFilename();
            String safeName = originalName.replaceAll("[^a-zA-Z0-9.-]", "_");
            String fileName = UUID.randomUUID() + "_" + safeName;

            // 2. 创建存储目录
            Path dir = Paths.get(uploadDir);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            // 保存文件
            Path filePath = dir.resolve(fileName);
            file.transferTo(filePath);
            return fileName; // 返回文件名用于数据库存储
        } catch (IOException e) {
            throw new RuntimeException("图片上传失败: " + e.getMessage());
        }
    }
    @Transactional
    public void updateProduct(Product updatedProduct, MultipartFile pictureFile) {
        // 1. 检查商品是否存在
        Product existingProduct = productMapper.selectById(updatedProduct.getId());

        // 2. 基础校验（复用添加时的校验方法）
        validateProduct(updatedProduct);

        // 3. 处理图片更新
        if (pictureFile != null && !pictureFile.isEmpty()) {
            // 删除旧图片（可选）
             deleteOldImage(existingProduct.getPicture());

            // 上传新图片
            String newPicturePath = uploadImage(pictureFile);
            updatedProduct.setPicture(newPicturePath);
        } else {
            // 保留原有图片路径
            updatedProduct.setPicture(existingProduct.getPicture());
        }

        // 4. 更新数据库
        productMapper.updateById(updatedProduct);
        // 5. 处理版本更新（先删除旧版本，再插入新版本）
        if (updatedProduct.getEditions() != null) {
            // 删除所有旧版本
            editionMapper.delete(new QueryWrapper<Edition>().eq("product_id", updatedProduct.getId()));

            // 插入新版本
            if (!updatedProduct.getEditions().isEmpty()) {
                updatedProduct.getEditions().forEach(edition -> edition.setProductId(updatedProduct.getId()));
                editionMapper.insertBatch(updatedProduct.getEditions()); // 批量插入
            }
        }
    }

    // 可选添加的旧图片删除方法
    private void deleteOldImage(String oldPicturePath) {
        if (oldPicturePath != null) {
            try {
                Path filePath = Paths.get(uploadDir).resolve(oldPicturePath);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("旧图片删除失败: " + e.getMessage());
            }
        }
    }

    // 单个删除
    @Transactional
    public void deleteProduct(Integer id) {
        // 1. 获取商品信息（包含图片路径）
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new IllegalArgumentException("商品不存在");
        }
        // 删除该商品的所有评论
        commentMapper.deleteByProductId(id);
        // 2. 删除图片
        deleteOldImage(product.getPicture());

        // 3. 删除数据库记录
        productMapper.deleteById(id);
    }
    // 批量删除
    @Transactional
    public void batchDeleteProducts(List<Integer> ids) {
        // 1. 批量查询商品信息
        List<Product> products = productMapper.selectBatchIds(ids);
        if (products.isEmpty()) return;
        // 新增：批量删除这些商品的所有评论

            // 使用 QueryWrapper 构建删除条件
            QueryWrapper<Comment> wrapper = new QueryWrapper<>();
            wrapper.in("product_id", ids);

            commentMapper.delete(wrapper);
            // 2. 删除所有关联图片
        products.stream()
                .map(Product::getPicture)
                .forEach(this::deleteOldImage);

        // 3. 批量删除数据库记录
        productMapper.deleteBatchIds(ids);
    }
    // 新增评论方法
    @Transactional
    public void addComment(Integer productId,String content, Integer userId) {
        // 验证商品存在
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new IllegalArgumentException("商品不存在");
        }

        // 构建评论对象
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setProductId(productId);
        comment.setCreateTime(LocalDateTime.now());
        comment.setLike(0);

        // 保存评论
        commentMapper.insertComment(comment);
    }
}



