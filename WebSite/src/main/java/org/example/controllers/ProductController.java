package org.example.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.example.dto.product.ProductCreateDTO;
import org.example.dto.product.ProductItemDTO;
import org.example.entities.ProductImageEntity;
import org.example.mappers.ProductMapper;
import org.example.repositories.ProductImageRepository;
import org.example.repositories.ProductRepository;
import org.example.storage.FileSaveFormat;
import org.example.storage.StorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/products")
@SecurityRequirement(name="my-api")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductMapper productMapper;
    private final StorageService storageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductItemDTO> create(@ModelAttribute ProductCreateDTO dto)
    {
        var p = productMapper.productByCreateProductDTO(dto);
        p.setImages(new ArrayList<>());
        productRepository.save(p);
        for (MultipartFile image : dto.getImages()) {
            var imageSave = storageService.saveImageFormat(image, FileSaveFormat.WEBP);
            var pi = ProductImageEntity
                    .builder()
                    .image(imageSave)
                    .product(p)
                    .build();
            productImageRepository.save(pi);
            p.getImages().add(pi);
        }
        return ResponseEntity.ok().body(productMapper.productToItemDTO(p));
    }
    @GetMapping()
    public ResponseEntity<List<ProductItemDTO>> getAllCategories() {
        return ResponseEntity.ok(productMapper.listProductsToItemDTO(productRepository.findAll()));
    }

}
