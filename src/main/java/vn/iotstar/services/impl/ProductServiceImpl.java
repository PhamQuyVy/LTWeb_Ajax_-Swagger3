package vn.iotstar.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.iotstar.entity.Product;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.services.IProductService;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final Path uploadDir = Paths.get("uploads/products");

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findByProductName(String name) {
        return productRepository.findByProductName(name);
    }

    @Override
    public Product create(Product data, MultipartFile imageFile) {
        data.setCreateDate(new Date());
        try {
            data.setImages(saveFile(imageFile));
        } catch (IOException e) {
            throw new RuntimeException("Khong the upload anh", e);
        }
        return productRepository.save(data);
    }

    @Override
    public Product update(Long productId, Product data, MultipartFile imageFile) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay product: " + productId));
        product.setProductName(data.getProductName());
        product.setQuantity(data.getQuantity());
        product.setUnitPrice(data.getUnitPrice());
        product.setDescription(data.getDescription());
        product.setDiscount(data.getDiscount());
        product.setStatus(data.getStatus());
        product.setCategory(data.getCategory());
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String oldImage = product.getImages();
                product.setImages(saveFile(imageFile));
                deleteFile(oldImage);
            }
        } catch (IOException e) {
            throw new RuntimeException("Khong the upload anh", e);
        }
        return productRepository.save(product);
    }

    @Override
    public void delete(Product entity) {
        deleteFile(entity.getImages());
        productRepository.delete(entity);
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
            System.err.println("Khong the xoa anh: " + fileName);
        }
    }
}
