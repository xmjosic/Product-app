package hr.xmjosic.product;

import static org.junit.jupiter.api.Assertions.*;

import hr.xmjosic.product.dao.ProductRepository;
import hr.xmjosic.product.dto.ErrorDto;
import hr.xmjosic.product.dto.ProductDto;
import hr.xmjosic.product.model.Product;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NoSuchElementException;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
class ProductApplicationTests {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private ProductRepository repository;

  @Test
  void createValidProduct() {
    ProductDto validProductDto =
            ProductDto.builder()
                    .name("Product 1")
                    .code("TEST167890")
                    .description("Description 1")
                    .priceEur(new BigDecimal(1))
                    .build();
    Product product =
        restTemplate.postForObject(
            "http://localhost:" + port + "/api/v1/product", validProductDto, Product.class);

    assertNotNull(product);
    assertNotNull(product.getId());
    assertEquals(validProductDto.name(), product.getName());
    assertEquals(validProductDto.code(), product.getCode());
    assertEquals(validProductDto.description(), product.getDescription());
    assertEquals(validProductDto.priceEur(), product.getPriceEur());
    assertNotNull(product.getPriceUsd());
    assertFalse(product.isAvailable());

    assertTrue(repository.existsById(product.getId()));
  }

  @Test
  void createProductWithInvalidCodeAndNegativePrice() {
    ProductDto productDto =
        ProductDto.builder()
            .name("Product 1")
            .code("inv")
            .description("Description 1")
            .priceEur(BigDecimal.ONE.negate())
            .isAvailable(true)
            .build();
    String path = "/api/v1/product";

    ErrorDto errorDto =
        restTemplate.postForObject("http://localhost:" + port + path, productDto, ErrorDto.class);

    assertNotNull(errorDto);
    assertNotNull(errorDto.timestamp());
    assertEquals(HttpStatus.BAD_REQUEST.value(), errorDto.status());
    assertEquals(MethodArgumentNotValidException.class.getName(), errorDto.error());
    assertTrue(errorDto.message().contains("priceEur: Price cannot be negative."));
    assertTrue(errorDto.message().contains("code: Code must be 10 characters long."));
    assertEquals(path, errorDto.path());
  }

  @Test
  void createProductWithNoCodeAndPriceEur() {
    ProductDto productDto =
        ProductDto.builder()
            .name("Product 1")
            .description("Description 1")
            .isAvailable(true)
            .build();
    String path = "/api/v1/product";

    ErrorDto errorDto =
        restTemplate.postForObject("http://localhost:" + port + path, productDto, ErrorDto.class);

    assertNotNull(errorDto);
    assertNotNull(errorDto.timestamp());
    assertEquals(HttpStatus.BAD_REQUEST.value(), errorDto.status());
    assertEquals(MethodArgumentNotValidException.class.getName(), errorDto.error());
    assertTrue(errorDto.message().contains("code: Code is mandatory."));
    assertTrue(errorDto.message().contains("priceEur: Price in euros is mandatory."));
    assertEquals(path, errorDto.path());
  }

  @Test
  void createProductWithExistingCode() {
    ProductDto validProductDto =
            ProductDto.builder()
                    .name("Product 1")
                    .code("TEST667890")
                    .description("Description 1")
                    .priceEur(new BigDecimal(1))
                    .build();

    Product product =
            restTemplate.postForObject(
                    "http://localhost:" + port + "/api/v1/product", validProductDto, Product.class);

    assertTrue(repository.existsById(product.getId()));
    assertEquals(validProductDto.code(), product.getCode());

    ProductDto nextValidProductDto =
            ProductDto.builder()
                    .name("Product 1")
                    .code("TEST667890")
                    .description("Description 1")
                    .priceEur(new BigDecimal(1))
                    .build();

    ErrorDto errorDto =
            restTemplate.postForObject(
                    "http://localhost:" + port + "/api/v1/product", nextValidProductDto, ErrorDto.class);

    assertNotNull(errorDto);
    assertNotNull(errorDto.timestamp());
    assertEquals(HttpStatus.BAD_REQUEST.value(), errorDto.status());
    assertEquals(ConstraintViolationException.class.getName(), errorDto.error());
    assertTrue(errorDto.message().contains("Product code must be unique"));
    assertEquals("/api/v1/product", errorDto.path());
  }

  @Test
  void updateProduct() {
    ProductDto validProductDto =
            ProductDto.builder()
                    .name("Product 1")
                    .code("TEST267890")
                    .description("Description 1")
                    .priceEur(new BigDecimal(1))
                    .build();

    Product product =
        restTemplate.postForObject(
            "http://localhost:" + port + "/api/v1/product", validProductDto, Product.class);

    ProductDto updatedProductDto =
        ProductDto.builder()
            .name("Product 1")
            .code("1234567890")
            .description("Description 1 - updated")
            .priceEur(BigDecimal.ONE)
            .isAvailable(true)
            .build();

    ResponseEntity<Product> response =
        restTemplate.exchange(
            "http://localhost:" + port + "/api/v1/product/" + product.getId(),
            HttpMethod.PUT,
            new HttpEntity<>(updatedProductDto, null),
            Product.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(updatedProductDto.name(), response.getBody().getName());
    assertEquals(updatedProductDto.code(), response.getBody().getCode());
    assertEquals(updatedProductDto.description(), response.getBody().getDescription());
    assertEquals(updatedProductDto.priceEur(), response.getBody().getPriceEur());
    assertNotNull(response.getBody().getPriceUsd());
    assertTrue(response.getBody().isAvailable());

    assertTrue(repository.existsById(product.getId()));
  }

  @Test
  void updateProductWithInvalidCodeAndNoPrice() {
    ProductDto validProductDto =
            ProductDto.builder()
                    .name("Product 1")
                    .code("TEST367890")
                    .description("Description 1")
                    .priceEur(new BigDecimal(1))
                    .build();

    Product product =
        restTemplate.postForObject(
            "http://localhost:" + port + "/api/v1/product", validProductDto, Product.class);

    ProductDto updatedProductDto =
        ProductDto.builder()
            .name("Product 1")
            .code("1234567890invalid")
            .description("Description 1 - updated")
            .build();

    ResponseEntity<ErrorDto> response =
        restTemplate.exchange(
            "http://localhost:" + port + "/api/v1/product/" + product.getId(),
            HttpMethod.PUT,
            new HttpEntity<>(updatedProductDto, null),
            ErrorDto.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertNotNull(response.getBody().timestamp());
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
    assertEquals(MethodArgumentNotValidException.class.getName(), response.getBody().error());
    assertTrue(response.getBody().message().contains("code: Code must be 10 characters long."));
    assertTrue(response.getBody().message().contains("priceEur: Price in euros is mandatory."));
    assertEquals("/api/v1/product/" + product.getId(), response.getBody().path());
  }

  @Test
  void readProduct() {
    ProductDto validProductDto =
            ProductDto.builder()
                    .name("Product 1")
                    .code("TEST467890")
                    .description("Description 1")
                    .priceEur(new BigDecimal(1))
                    .build();

    Product product =
        restTemplate.postForObject(
            "http://localhost:" + port + "/api/v1/product", validProductDto, Product.class);

    Product retVal =
        restTemplate.getForObject(
            "http://localhost:" + port + "/api/v1/product/" + product.getId(), Product.class);

    assertTrue(repository.existsById(product.getId()));

    assertNotNull(retVal);
    assertEquals(retVal.getName(), product.getName());
    assertEquals(retVal.getCode(), product.getCode());
    assertEquals(retVal.getDescription(), product.getDescription());
    assertEquals(
        BigDecimal.valueOf(retVal.getPriceEur().doubleValue()),
        BigDecimal.valueOf(product.getPriceEur().doubleValue()));
    assertEquals(
        BigDecimal.valueOf(retVal.getPriceUsd().doubleValue()).setScale(2, RoundingMode.UP),
        BigDecimal.valueOf(product.getPriceUsd().doubleValue()).setScale(2, RoundingMode.UP));
    assertEquals(retVal.isAvailable(), product.isAvailable());
  }

  @Test
  void readProductWithNotExistingId() {
    ResponseEntity<ErrorDto> retVal =
        restTemplate.getForEntity(
            "http://localhost:" + port + "/api/v1/product/1234567890", ErrorDto.class);

    assertFalse(repository.existsById(1234567890L));

    assertEquals(HttpStatus.NOT_FOUND, retVal.getStatusCode());
    assertNotNull(retVal.getBody());
    assertNotNull(retVal.getBody().timestamp());
    assertEquals(HttpStatus.NOT_FOUND.value(), retVal.getBody().status());
    assertEquals(NoSuchElementException.class.getName(), retVal.getBody().error());
    assertTrue(retVal.getBody().message().contains("Product with id 1234567890 not found."));
    assertEquals("/api/v1/product/1234567890", retVal.getBody().path());
  }

  @Test
  void deleteProduct() {
    ProductDto validProductDto =
            ProductDto.builder()
                    .name("Product 1")
                    .code("TEST567890")
                    .description("Description 1")
                    .priceEur(new BigDecimal(1))
                    .build();

    Product product =
        restTemplate.postForObject(
            "http://localhost:" + port + "/api/v1/product", validProductDto, Product.class);

    assertTrue(repository.existsById(product.getId()));

    ResponseEntity<Void> response =
        restTemplate.exchange(
            "http://localhost:" + port + "/api/v1/product/" + product.getId(),
            HttpMethod.DELETE,
            null,
            Void.class);

    assertFalse(repository.existsById(product.getId()));

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }
}
