package com.bac.bac.controller;

import com.bac.bac.exception.ResourceNotFoundException;
import com.bac.bac.model.Product;
import com.bac.bac.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 *
 */
@RestController
@RequestMapping(name = "/api")
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping(value = "/v1/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> retrieveAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping(value = "/v1/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> retrieveProduct(@PathVariable int id) {
        Optional<Product> product = productRepository.findById(id);

        if (!product.isPresent())
            throw new ResourceNotFoundException("Product", "id", id);

        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/v1/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteProduct(@PathVariable int id) {
        productRepository.deleteById(id);
    }

    @PostMapping(value = "/v1/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createProduct(@Valid @RequestBody Product product) {
        boolean existProduct = productRepository.existsById(Math.toIntExact(product.getId()));

        if (existProduct) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "EL Producto con Id " + product.getId() + " ya existe!");
        }

        return new ResponseEntity<>(productRepository.save(product), HttpStatus.CREATED);

    }
}
