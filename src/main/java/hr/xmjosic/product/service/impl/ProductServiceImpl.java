package hr.xmjosic.product.service.impl;

import hr.xmjosic.product.converter.ProductConverter;
import hr.xmjosic.product.dao.ProductRepository;
import hr.xmjosic.product.dto.ProductDto;
import hr.xmjosic.product.model.Product;
import hr.xmjosic.product.service.HnbApiService;
import hr.xmjosic.product.service.ProductService;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Implementation of {@link ProductService}. */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

  /** Converts between {@link ProductDto} and {@link Product}. */
  private final ProductConverter converter;

  /** Repository for {@link Product}. */
  private final ProductRepository repository;

  /** Service for HNB API. */
  private final HnbApiService hnbApiService;

  @Override
  public Product create(ProductDto productDto) {
    log.info("Creating product: {}", productDto);
    Product product = converter.convert(productDto);
    Objects.requireNonNull(product, "Product cannot be null.");
    setUsdPrice(product);
    log.debug("Calling repository to save product.");
    final Product retVal = repository.saveAndFlush(product);
    log.debug("Saved product: {}", retVal);
    return retVal;
  }

  @Override
  public Product read(Long id) {
    log.info("Retrieving product: {}", id);
    Optional<Product> byId = repository.findById(id);
    log.debug("Retrieved product: {}", byId.isPresent());
    return byId.orElseThrow(
        () -> new IllegalArgumentException("Product with id " + id + " not found."));
  }

  @Override
  public Product update(Long id, ProductDto productDto) {
    log.info("Updating product with id {}: {}", id, productDto);
    Product product = converter.convert(productDto);
    Objects.requireNonNull(product, "Product cannot be null.");
    product.setId(id);
    setUsdPrice(product);
    log.debug("Calling repository to update product.");
    final Product retVal = repository.saveAndFlush(product);
    log.debug("Updated product: {}", retVal);
    return retVal;
  }

  @Override
  public void delete(Long id) {
    log.info("Deleting product: {}", id);
    repository.deleteById(id);
  }

  /**
   * Sets the price in USD for the given product based on the EUR price and the USD selling rate.
   *
   * @param product the product for which to set the USD price
   */
  private void setUsdPrice(Product product) {
    log.debug("Setting price in USD.");
    product.setPriceUsd(product.getPriceEur().multiply(hnbApiService.getUsdSellingRate()));
  }
}
