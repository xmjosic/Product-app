package hr.xmjosic.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hr.xmjosic.product.converter.ProductConverter;
import hr.xmjosic.product.dao.ProductRepository;
import hr.xmjosic.product.dto.ProductDto;
import hr.xmjosic.product.model.Product;
import hr.xmjosic.product.service.impl.ProductServiceImpl;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  private ProductService service;

  @Mock private ProductConverter converter;
  @Mock private ProductRepository repository;
  @Mock private HnbApiService hnbApiService;

  @BeforeEach
  void setUp() {
    service = new ProductServiceImpl(converter, repository, hnbApiService);
  }

  @Test
  void create() {
    ProductDto productDto =
        new ProductDto(
            "1234567890", "Test Product", BigDecimal.valueOf(10.0), "Test Description", true);
    Product product = new Product();
    product.setCode("1234567890");
    product.setName("Test Product");
    product.setPriceEur(BigDecimal.valueOf(10.0));
    product.setDescription("Test Description");
    product.setAvailable(true);

    when(converter.convert(productDto)).thenReturn(product);
    when(repository.saveAndFlush(product)).thenReturn(product);
    when(hnbApiService.getUsdSellingRate()).thenReturn(BigDecimal.valueOf(1.1));

    Product createdProduct = service.create(productDto);

    assertNotNull(createdProduct);
    assertEquals("1234567890", createdProduct.getCode());
    verify(repository, times(1)).saveAndFlush(product);
  }

  @Test
  void read() {
    Long id = 1L;
    Product product = new Product();
    product.setId(id);

    when(repository.findById(id)).thenReturn(Optional.of(product));

    Product readProduct = service.read(id);

    assertNotNull(readProduct);
    assertEquals(id, readProduct.getId());
    verify(repository, times(1)).findById(id);
  }

  @Test
  void update() {
    Long id = 1L;
    ProductDto productDto =
        new ProductDto(
            "1234567890", "Updated Product", BigDecimal.valueOf(20.0), "Updated Description", true);
    Product product = new Product();
    product.setId(id);
    product.setCode("1234567890");
    product.setName("Updated Product");
    product.setPriceEur(BigDecimal.valueOf(20.0));
    product.setDescription("Updated Description");
    product.setAvailable(true);

    when(converter.convert(productDto)).thenReturn(product);
    when(repository.saveAndFlush(product)).thenReturn(product);
    when(hnbApiService.getUsdSellingRate()).thenReturn(BigDecimal.valueOf(1.1));

    Product updatedProduct = service.update(id, productDto);

    assertNotNull(updatedProduct);
    assertEquals("1234567890", updatedProduct.getCode());
    verify(repository, times(1)).saveAndFlush(product);
  }

  @Test
  void delete() {
    Long id = 1L;

    doNothing().when(repository).deleteById(id);

    service.delete(id);

    verify(repository, times(1)).deleteById(id);
  }
}
