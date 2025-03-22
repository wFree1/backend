package org.csu.gameshop.service;

import org.csu.gameshop.entity.Product;
import org.csu.gameshop.vo.ProductVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
   public ProductVO getProductDetail(int productId);
   public List<ProductVO> getProductList();
   public void addProduct(Product product, MultipartFile pictureFile);
   List<ProductVO> getAllProducts();
}
