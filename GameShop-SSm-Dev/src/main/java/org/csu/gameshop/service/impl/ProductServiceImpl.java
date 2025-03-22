package org.csu.gameshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.gameshop.entity.Product;
import org.csu.gameshop.persistence.ProductMapper;
import org.csu.gameshop.service.ProductService;
import org.csu.gameshop.vo.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductVO getProductDetail(int productId) {
       ProductVO productVO = new ProductVO();
       Product product = productMapper.selectById(productId);
        BeanUtils.copyProperties(product, productVO); // 自动复制同名属性
        return productVO;
    }

    @Override
    public List<ProductVO> getProductList() {
        return List.of();
    }

    @Override
    public List<ProductVO> getAllProducts() {
        List<Product> products = productMapper.selectList(null);
        return convertToVOList(products);
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
