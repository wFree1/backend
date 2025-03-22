package org.csu.gameshop.controller;

import org.csu.gameshop.entity.Product;
import org.csu.gameshop.service.ProductService;
import org.csu.gameshop.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/main")  // 添加此方法
    public ResponseEntity<List<Product>> productMain(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public String productDetail(Integer id, Model model)
    {
        Product product=productService.getProductDetail(id);
        model.addAttribute("product", product);
        return "/product/product";
    }


       /* @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<?> addProduct(
                @RequestPart("product") Product product,      // 接收商品表单数据
                @RequestPart(value = "image", required = false) MultipartFile imageFile
        ) {
            try {
                productService.addProduct(product, imageFile);
                return ResponseEntity.ok("商品添加成功");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (RuntimeException e) {
                return ResponseEntity.internalServerError().body("服务器错误: " + e.getMessage());
            }
        }*/
    }


