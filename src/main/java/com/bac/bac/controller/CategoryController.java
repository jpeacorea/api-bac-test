package com.bac.bac.controller;

import com.bac.bac.exception.ResourceNotFoundException;
import com.bac.bac.model.Category;
import com.bac.bac.model.Product;
import com.bac.bac.repository.CategoryRepository;
import com.bac.bac.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(name = "/api")
public class CategoryController {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    // GuestRepository guestRepository;

    @GetMapping(value = "/v1/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> retrieveAllProducts() {
        return categoryRepository.findAll();
    }

    @GetMapping(value = "/v1/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> retrieveAdministrator(@PathVariable int id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (!category.isPresent())
            throw new ResourceNotFoundException("Category", "id", id);

        return new ResponseEntity<>(category.get(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/v1/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteAdministrator(@PathVariable int id) {
        categoryRepository.deleteById(id);
    }

    @PostMapping(value = "/v1/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAdministrator(@Valid @RequestBody Category Category) {
        boolean existProduct = categoryRepository.existsById(Math.toIntExact(Category.getId()));

        if (existProduct) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "EL Producto con Id " + Category.getId() + " ya existe!");
        }

        return new ResponseEntity<>(categoryRepository.save(Category), HttpStatus.CREATED);

    }
    @PostMapping(value = "/v1/categories/{id}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createProduct(@PathVariable int id, @RequestBody Product product) {

        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if(!categoryOptional.isPresent()) {
            throw new ResourceNotFoundException("Category", "id", id);
        }

        Category category = categoryOptional.get();

        product.setCategory(category);

        productRepository.save(product);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).build();

    }

    @PutMapping(value = "/v1/categories/{id}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") int id, @RequestBody Product product) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if(!categoryOptional.isPresent()) {
            throw new ResourceNotFoundException("Category", "id", id);
        }

        product.setName(product.getName());
        product.setCategory(categoryOptional.get());

        return new ResponseEntity<>(productRepository.save(product), HttpStatus.NO_CONTENT);

    }

    @PutMapping(value = "/v1/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateCategory(@PathVariable(value = "id") int id, @Valid @RequestBody Category categoryDetails) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());

        return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.NO_CONTENT);
    }
}
