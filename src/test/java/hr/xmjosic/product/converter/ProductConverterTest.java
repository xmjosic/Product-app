package hr.xmjosic.product.converter;

import static org.junit.jupiter.api.Assertions.*;

import hr.xmjosic.product.dto.ProductDto;
import hr.xmjosic.product.model.Product;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductConverterTest {

  @Test
  void convertValidProductDtoToProduct() {
    ProductConverter converter = new ProductConverter();
    ProductDto dto =
        new ProductDto(
            "1234567890", "Test Product", new BigDecimal("10.00"), "Test Description", true);
    Product product = converter.convert(dto);
    assertNotNull(product);
    assertEquals(dto.code(), product.getCode());
    assertEquals(dto.name(), product.getName());
    assertEquals(dto.priceEur(), product.getPriceEur());
    assertEquals(dto.description(), product.getDescription());
    assertEquals(dto.isAvailable(), product.isAvailable());
  }

  @Test
  void convertNullProductDtoToProduct() {
    ProductConverter converter = new ProductConverter();
    NullPointerException npe =
        assertThrows(NullPointerException.class, () -> converter.convert(null));
    assertEquals("ProductDto cannot be null.", npe.getMessage());
  }
}
