package org.csu.gameshop.service;

import org.csu.gameshop.entity.Product;
import org.csu.gameshop.vo.ProductVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
   public Product getProductDetail(int productId);
   public List<Product> getProductList();
   public void addProduct(Product product, MultipartFile pictureFile);
   List<Product> getAllProducts();
   public void updateProduct(Product updatedProduct, MultipartFile pictureFile);
   public void deleteProduct(Integer id);
    public void batchDeleteProducts(List<Integer> ids);
}
