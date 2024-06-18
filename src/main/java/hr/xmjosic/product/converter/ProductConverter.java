package hr.xmjosic.product.converter;

import hr.xmjosic.product.dto.ProductDto;
import hr.xmjosic.product.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

/** Converts between {@link ProductDto} and {@link Product}. */
@Component
@Slf4j
public class ProductConverter implements Converter<ProductDto, Product> {

  @Override
  public Product convert(ProductDto source) {
    Objects.requireNonNull(source, "ProductDto cannot be null.");
    log.debug("Converting ProductDto to Product: {}", source);
    Product retVal = new Product();
    retVal.setCode(source.code());
    retVal.setName(source.name());
    retVal.setPriceEur(source.priceEur());
    retVal.setDescription(source.description());
    retVal.setAvailable(source.isAvailable());
    log.debug("Converted ProductDto to Product: {}", retVal);
    return retVal;
  }
}
