package org.csu.gameshop.controller;

import org.csu.gameshop.entity.Product;
import org.csu.gameshop.service.ProductService;
import org.csu.gameshop.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/product/main")  // 添加此方法
    public String productMain(Model model) {
        List<ProductVO> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product/main"; // 返回模板路径
    }

    @GetMapping("/product/{id}")
    public String productDetail(Integer id, Model model)
    {
        ProductVO productVO=productService.getProductDetail(id);
        model.addAttribute("product", productVO);
        return "/product/product";
    }

}
