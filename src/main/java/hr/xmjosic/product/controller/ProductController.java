package hr.xmjosic.product.controller;

import hr.xmjosic.product.dto.ProductDto;
import hr.xmjosic.product.model.Product;
import hr.xmjosic.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Controller for product-related operations. */
@CrossOrigin
@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

  /** The service for product-related operations. */
  private final ProductService service;

  /**
   * Creates a new product based on the provided product data.
   *
   * @param productDto the data of the product to be created
   * @return the newly created product
   */
  @Operation(summary = "Creates a new product")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Product createProduct(@Valid @RequestBody ProductDto productDto) {
    log.trace("Accessing createProduct method");
    return this.service.create(productDto);
  }

  /**
   * Retrieves a product by its ID.
   *
   * @param id the ID of the product to retrieve
   * @return the product with the given ID, or null if not found
   */
  @Operation(summary = "Retrieves a product by its ID")
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Product readProduct(@PathVariable Long id) {
    log.trace("Accessing readProduct method");
    return this.service.read(id);
  }

  /**
   * Updates a product with the given ID using the provided product data.
   *
   * @param id the ID of the product to update
   * @param productDto the data of the product to update
   * @return the updated product
   */
  @Operation(summary = "Updates a product with the given ID using the provided product data")
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Product updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
    log.trace("Accessing updateProduct method");
    return this.service.update(id, productDto);
  }

  /**
   * Deletes a product with the given ID from the database.
   *
   * @param id the ID of the product to be deleted
   */
  @Operation(summary = "Deletes a product with the given ID from the database")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteProduct(@PathVariable Long id) {
    log.trace("Accessing deleteProduct method");
    this.service.delete(id);
  }
}
