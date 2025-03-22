package org.csu.gameshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.micrometer.common.util.StringUtils;
import org.csu.gameshop.entity.Product;
import org.csu.gameshop.persistence.ProductMapper;
import org.csu.gameshop.service.ProductService;
import org.csu.gameshop.vo.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
}
    /**
     * 添加单个商品
     *
     * @param product     商品实体（不含图片路径）
     * @param pictureFile 图片文件（允许为空）
     */
   /* @Transactional
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
            // 生成唯一文件名
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            // 定义存储路径（示例路径：/var/www/uploads/）
            Path uploadDir = Paths.get("/images");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            // 保存文件
            Path filePath = uploadDir.resolve(fileName);
            file.transferTo(filePath);
            return fileName; // 返回文件名用于数据库存储
        } catch (IOException e) {
            throw new RuntimeException("图片上传失败: " + e.getMessage());
        }
    }
}


*/