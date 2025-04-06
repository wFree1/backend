package org.csu.gameshopms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.Arrays;
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
     * @param imageFiles 图片文件（允许为空）
     */
    @Transactional
    public void addProduct(Product product, MultipartFile[] imageFiles) {
        System.out.println("接收到的版本信息: " + product.getEditions()); // 添加日志
        // 1. 基础校验
        validateProduct(product);

        // 2. 处理图片上传（最多 5 张）
        if (imageFiles != null && imageFiles.length > 0) {
            if (imageFiles.length > 5) {
                throw new IllegalArgumentException("最多上传 5 张图片");
            }

            for (int i = 0; i < imageFiles.length; i++) {
                if (i >= 5) break; // 最多处理前 5 张
                MultipartFile file = imageFiles[i];
                if (!file.isEmpty()) {
                    String picturePath = uploadImage(file);
                    // 根据索引设置到不同字段
                    switch (i) {
                        case 0: product.setPicture1(picturePath); break;
                        case 1: product.setPicture2(picturePath); break;
                        case 2: product.setPicture3(picturePath); break;
                        case 3: product.setPicture4(picturePath); break;
                        case 4: product.setPicture5(picturePath); break;
                    }
                }
            }
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
    public void updateProduct(Product updatedProduct, MultipartFile[] imageFiles, String imageOrder) {
        // 1. 检查商品是否存在
        Product existingProduct = productMapper.selectById(updatedProduct.getId());
        if (existingProduct == null) {
            throw new IllegalArgumentException("商品不存在");
        }
        // 2. 基础校验
        validateProduct(updatedProduct);

        // 3. 解析 imageOrder（前端传来的 JSON 字符串，数组长度固定为5，
        // 每个元素为该槽预览 URL；如果为空字符串，则表示该槽已删除）
        List<String> newOrder;
        try {
            ObjectMapper mapper = new ObjectMapper();
            newOrder = mapper.readValue(imageOrder, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("解析 imageOrder 失败: " + e.getMessage());
        }
        if (newOrder.size() != 5) {
            throw new IllegalArgumentException("imageOrder 长度必须为 5");
        }

        // 3. 对于每个图片槽 i=0..4，更新图片字段
        for (int i = 0; i < 5; i++) {
            MultipartFile file = imageFiles[i];
            String orderEntry = newOrder.get(i);
            if (file != null && !file.isEmpty()) {
                // 上传新图片，更新对应槽
                String newPath = uploadImage(file);
                setPictureField(updatedProduct, i, newPath);
            } else {
                if (orderEntry.isEmpty()) {
                    // 前端表示该槽已删除，则置为 null
                    setPictureField(updatedProduct, i, null);
                } else {
                    // 未上传新文件且槽未删除，则根据 newOrder 中的 URL 提取文件名更新
                    String fileName = extractFileName(orderEntry);
                    setPictureField(updatedProduct, i, fileName);
                }
            }
        }

        // 4. 更新数据库记录
        productMapper.updateById(updatedProduct);
        // 5. 更新版本信息（先删除旧版本，再插入新版本）
        if (updatedProduct.getEditions() != null) {
            editionMapper.delete(new QueryWrapper<Edition>().eq("product_id", updatedProduct.getId()));
            if (!updatedProduct.getEditions().isEmpty()) {
                updatedProduct.getEditions().forEach(edition -> edition.setProductId(updatedProduct.getId()));
                editionMapper.insertBatch(updatedProduct.getEditions());
            }
        }
    }

    // 辅助方法：根据槽位设置图片字段
    private void setPictureField(Product product, int index, String path) {
        switch (index) {
            case 0: product.setPicture1(path); break;
            case 1: product.setPicture2(path); break;
            case 2: product.setPicture3(path); break;
            case 3: product.setPicture4(path); break;
            case 4: product.setPicture5(path); break;
        }
    }

    // 辅助方法：从 URL 中提取图片文件名
    private String extractFileName(String url) {
        if (url == null || url.isEmpty()) return "";
        int lastSlash = url.lastIndexOf('/');
        if (lastSlash != -1 && lastSlash < url.length() - 1) {
            return url.substring(lastSlash + 1);
        }
        return url;
    }
    // 删除旧图片（根据需求实现）
    private void deleteOldPictures(Product existingProduct) {
        deleteFile(existingProduct.getPicture1());
        deleteFile(existingProduct.getPicture2());
        deleteFile(existingProduct.getPicture3());
        deleteFile(existingProduct.getPicture4());
        deleteFile(existingProduct.getPicture5());
    }


    private void deleteFile(String filename) {
        if (filename != null && !filename.isEmpty()) {
            Path path = Paths.get(uploadDir, filename);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new RuntimeException("删除旧图片失败: " + filename);
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

        // 新增：删除所有子种类记录
        QueryWrapper<Edition> editionWrapper = new QueryWrapper<>();
        editionWrapper.eq("product_id", id);
        editionMapper.delete(editionWrapper);

        // 2. 删除所有图片（picture1~picture5）
        deleteOldPictures(product);

        // 3. 删除数据库记录
        productMapper.deleteById(id);
    }

    // 批量删除

    @Transactional
    public void batchDeleteProducts(List<Integer> ids) {
        // 1. 批量查询商品信息
        List<Product> products = productMapper.selectBatchIds(ids);
        if (products.isEmpty()) return;

        // 批量删除这些商品的所有评论
        QueryWrapper<Comment> commentWrapper = new QueryWrapper<>();
        commentWrapper.in("product_id", ids);
        commentMapper.delete(commentWrapper);

        // 新增：批量删除所有子种类记录
        QueryWrapper<Edition> editionWrapper = new QueryWrapper<>();
        editionWrapper.in("product_id", ids);
        editionMapper.delete(editionWrapper);

        // 2. 删除所有关联图片
        products.forEach(this::deleteOldPictures);

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



