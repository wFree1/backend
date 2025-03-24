package org.csu.gameshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.micrometer.common.util.StringUtils;
import org.csu.gameshop.entity.Product;
import org.csu.gameshop.persistence.ProductMapper;
import org.csu.gameshop.service.ProductService;
import org.csu.gameshop.vo.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Value("${image.upload-dir}")
    private String uploadDir; // 从配置文件中注入路径

    @Override
    public Product getProductDetail(int productId) {
        Product product = productMapper.selectById(productId);
        return product;
    }

    @Override
    public List<Product> getProductList() {
        return List.of();
    }

    @Override
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
        // 1. 基础校验
        validateProduct(product);

        // 2. 处理图片上传
        if (pictureFile != null && !pictureFile.isEmpty()) {
            String picturePath = uploadImage(pictureFile);
            product.setPicture(picturePath);
        }

        // 3. 插入数据库
        productMapper.insert(product);
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

        // 2. 删除所有关联图片
        products.stream()
                .map(Product::getPicture)
                .forEach(this::deleteOldImage);

        // 3. 批量删除数据库记录
        productMapper.deleteBatchIds(ids);
    }

}



