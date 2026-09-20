package vn.iotstar.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.iotstar.entity.Category;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.services.ICategoryService;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final Path uploadDir = Paths.get("uploads/categories");

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findByCategoryName(String name) {
        return categoryRepository.findByCategoryName(name);
    }

    @Override
    public Category create(String categoryName, MultipartFile icon) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        try {
            category.setIcon(saveFile(icon));
        } catch (IOException e) {
            throw new RuntimeException("Khong the upload icon", e);
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long categoryId, String categoryName, MultipartFile icon) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay category: " + categoryId));
        category.setCategoryName(categoryName);
        try {
            if (icon != null && !icon.isEmpty()) {
                String oldIcon = category.getIcon();
                category.setIcon(saveFile(icon));
                deleteFile(oldIcon);
            }
        } catch (IOException e) {
            throw new RuntimeException("Khong the upload icon", e);
        }
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Category entity) {
        deleteFile(entity.getIcon());
        categoryRepository.delete(entity);
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        Files.createDirectories(uploadDir);
        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + ext;
        Path target = uploadDir.resolve(fileName);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return fileName;
    }

    private void deleteFile(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return;
        }
        try {
            Files.deleteIfExists(uploadDir.resolve(fileName));
        } catch (IOException e) {
            System.err.println("Khong the xoa icon: " + fileName);
        }
    }
}
