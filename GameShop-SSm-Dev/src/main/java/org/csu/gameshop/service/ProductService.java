package org.csu.gameshop.service;

import org.csu.gameshop.entity.Product;
import org.csu.gameshop.vo.ProductVO;

import java.util.List;

public interface ProductService {
   public ProductVO getProductDetail(int productId);
   public List<ProductVO> getProductList();

   List<ProductVO> getAllProducts();
}
