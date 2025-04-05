package org.csu.gameshopms.controller;

import org.csu.gameshopms.entity.Product;
import org.csu.gameshopms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(products);
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Product productDetail(@PathVariable Integer id)
    {
        return productService.getProductDetail(id);
    }
   /* // 新增图片获取接口
    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {aaaaaaaaa
            // 1. 从存储路径加载文件
            Path filePath = Paths.get("src/main/resources/static/images/").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            // 2. 校验文件是否存在且可读
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // 根据实际类型调整
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(null);
        }
    }*/
// 新增评论接口
   @PostMapping(value = "/{productId}/comments",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   public ResponseEntity<?> addComment(
           @PathVariable Integer productId,
           @RequestParam("content") String content,
           @RequestHeader("X-User-Id") Integer userId) {

       productService.addComment(productId, content, userId);
       return ResponseEntity.ok("评论添加成功");
   }

      //添加商品
        @PostMapping(value = "/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<?> addProduct(
                @RequestPart("product") Product product,      // 接收商品表单数据
                @RequestPart(value = "image", required = false) MultipartFile[] imageFiles
        ) {
            try {
                productService.addProduct(product, imageFiles);
                return ResponseEntity.ok("商品添加成功");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (RuntimeException e) {
                return ResponseEntity.internalServerError().body("服务器错误: " + e.getMessage());
            }
        }

    // 单个删除
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("商品删除成功");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("服务器错误: " + e.getMessage());
        }
    }
    // 批量删除（使用POST+RequestBody更通用）
    @PostMapping("/batch-delete")
    public ResponseEntity<?> batchDeleteProducts(@RequestBody List<Integer> ids) {
        try {
            productService.batchDeleteProducts(ids);
            return ResponseEntity.ok("批量删除成功");
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("服务器错误: " + e.getMessage());
        }
    }
        //修改商品信息
        @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<?> updateProduct(
                @PathVariable Integer id,
                @RequestPart("product") Product updatedProduct,
                @RequestPart(value = "image", required = false) MultipartFile[] imageFiles
        ) {
            try {
                updatedProduct.setId(id); // 确保ID一致性
                productService.updateProduct(updatedProduct, imageFiles);
                return ResponseEntity.ok("商品更新成功");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (RuntimeException e) {
                return ResponseEntity.internalServerError().body("服务器错误: " + e.getMessage());
            }
        }
    }
