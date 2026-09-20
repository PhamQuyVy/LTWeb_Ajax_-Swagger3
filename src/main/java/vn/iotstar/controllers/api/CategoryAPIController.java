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
import vn.iotstar.model.Response;
import vn.iotstar.services.ICategoryService;

@RestController
@RequestMapping(path = "/api/category")
@Tag(name = "Category API", description = "CRUD danh muc san pham")
public class CategoryAPIController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        return new ResponseEntity<>(new Response(true, "Thanh cong", categoryService.findAll()), HttpStatus.OK);
    }

    @PostMapping(path = "/getCategory")
    public ResponseEntity<?> getCategory(@RequestParam("id") Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (category.isPresent()) {
            return new ResponseEntity<>(new Response(true, "Thanh cong", category.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Response(false, "Khong tim thay", null), HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/addCategory")
    public ResponseEntity<?> addCategory(@RequestParam("categoryName") String categoryName,
            @RequestParam(value = "icon", required = false) MultipartFile icon) {
        Optional<Category> optCategory = categoryService.findByCategoryName(categoryName);
        if (optCategory.isPresent()) {
            return new ResponseEntity<>(new Response(false, "Category da ton tai trong he thong", null),
                    HttpStatus.BAD_REQUEST);
        }
        Category category = categoryService.create(categoryName, icon);
        return new ResponseEntity<>(new Response(true, "Them thanh cong", category), HttpStatus.OK);
    }

    @PutMapping(path = "/updateCategory")
    public ResponseEntity<?> updateCategory(@RequestParam("categoryId") Long categoryId,
            @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "icon", required = false) MultipartFile icon) {
        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Khong tim thay Category", null), HttpStatus.BAD_REQUEST);
        }
        Category category = categoryService.update(categoryId, categoryName, icon);
        return new ResponseEntity<>(new Response(true, "Cap nhat thanh cong", category), HttpStatus.OK);
    }

    @DeleteMapping(path = "/deleteCategory")
    public ResponseEntity<?> deleteCategory(@RequestParam("categoryId") Long categoryId) {
        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Khong tim thay Category", null), HttpStatus.BAD_REQUEST);
        }
        categoryService.delete(optCategory.get());
        return new ResponseEntity<>(new Response(true, "Xoa thanh cong", optCategory.get()), HttpStatus.OK);
    }
}
