package hr.xmjosic.product.service;

import hr.xmjosic.product.dto.ProductDto;
import hr.xmjosic.product.model.Product;

/** Service for product-related operations. */
public interface ProductService {

  /**
   * Creates a new product based on the provided product data.
   *
   * @param productDto the data of the product to be created
   * @return the newly created product
   */
  Product create(ProductDto productDto);

  /**
   * Reads a product by its ID.
   *
   * @param id the ID of the product to read
   * @return the product with the given ID, or null if not found
   */
  Product read(Long id);

  /**
   * Updates a product with the given ID using the provided product data.
   *
   * @param id the ID of the product to update
   * @param productDto the data of the product to update
   * @return the updated product, or null if the update failed
   */
  Product update(Long id, ProductDto productDto);

  /**
   * Deletes a product with the given ID from the database.
   *
   * @param id the ID of the product to be deleted
   */
  void delete(Long id);
}
