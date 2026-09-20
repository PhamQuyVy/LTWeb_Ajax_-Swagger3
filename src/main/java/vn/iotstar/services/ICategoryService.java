package vn.iotstar.services;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import vn.iotstar.entity.Category;

public interface ICategoryService {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Optional<Category> findByCategoryName(String name);
    Category create(String categoryName, MultipartFile icon);
    Category update(Long categoryId, String categoryName, MultipartFile icon);
    void delete(Category entity);
}
