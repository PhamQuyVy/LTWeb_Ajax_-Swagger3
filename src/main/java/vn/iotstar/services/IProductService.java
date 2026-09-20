package vn.iotstar.services;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import vn.iotstar.entity.Product;

public interface IProductService {
    List<Product> findAll();
    Optional<Product> findById(Long id);
    Optional<Product> findByProductName(String name);
    Product create(Product data, MultipartFile imageFile);
    Product update(Long productId, Product data, MultipartFile imageFile);
    void delete(Product entity);
}
