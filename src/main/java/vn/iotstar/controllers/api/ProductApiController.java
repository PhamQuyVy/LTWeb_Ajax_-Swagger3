package vn.iotstar.controllers.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.tags.Tag;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.model.Response;
import vn.iotstar.services.ICategoryService;
import vn.iotstar.services.IProductService;

@RestController
@RequestMapping(path = "/api/product")
@Tag(name = "Product API", description = "CRUD san pham")
public class ProductApiController {

    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllProduct() {
        return new ResponseEntity<>(new Response(true, "Thanh cong", productService.findAll()), HttpStatus.OK);
    }

    @PostMapping(path = "/getProduct")
    public ResponseEntity<?> getProduct(@RequestParam("id") Long id) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            return new ResponseEntity<>(new Response(true, "Thanh cong", product.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Response(false, "Khong tim thay", null), HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/addProduct")
    public ResponseEntity<?> addProduct(
            @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam("unitPrice") Double unitPrice,
            @RequestParam(value = "discount", defaultValue = "0") Double discount,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("status") Short status) {

        Optional<Product> optProduct = productService.findByProductName(productName);
        if (optProduct.isPresent()) {
            return new ResponseEntity<>(new Response(false, "San pham nay da ton tai trong he thong", optProduct.get()),
                    HttpStatus.BAD_REQUEST);
        }
        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Category khong ton tai", null), HttpStatus.BAD_REQUEST);
        }

        Product product = new Product();
        product.setProductName(productName);
        product.setUnitPrice(unitPrice);
        product.setDiscount(discount);
        product.setDescription(description);
        product.setCategory(optCategory.get());
        product.setQuantity(quantity);
        product.setStatus(status);

        Product saved = productService.create(product, imageFile);
        return new ResponseEntity<>(new Response(true, "Thanh cong", saved), HttpStatus.OK);
    }

    @PutMapping(path = "/updateProduct")
    public ResponseEntity<?> updateProduct(
            @RequestParam("productId") Long productId,
            @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam("unitPrice") Double unitPrice,
            @RequestParam(value = "discount", defaultValue = "0") Double discount,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("status") Short status) {

        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Khong tim thay san pham", null), HttpStatus.BAD_REQUEST);
        }
        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Category khong ton tai", null), HttpStatus.BAD_REQUEST);
        }

        Product data = new Product();
        data.setProductName(productName);
        data.setUnitPrice(unitPrice);
        data.setDiscount(discount);
        data.setDescription(description);
        data.setCategory(optCategory.get());
        data.setQuantity(quantity);
        data.setStatus(status);

        Product updated = productService.update(productId, data, imageFile);
        return new ResponseEntity<>(new Response(true, "Cap nhat thanh cong", updated), HttpStatus.OK);
    }

    @DeleteMapping(path = "/deleteProduct")
    public ResponseEntity<?> deleteProduct(@RequestParam("productId") Long productId) {
        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Khong tim thay san pham", null), HttpStatus.BAD_REQUEST);
        }
        productService.delete(optProduct.get());
        return new ResponseEntity<>(new Response(true, "Xoa thanh cong", optProduct.get()), HttpStatus.OK);
    }
}
