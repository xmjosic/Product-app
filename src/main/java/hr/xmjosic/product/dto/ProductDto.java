package hr.xmjosic.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * Used to transfer data about products
 *
 * @param code the code of the product
 * @param name the name of the product
 * @param priceEur the price of the product in euros
 * @param description the description of the product
 * @param isAvailable true if the product is available
 */
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductDto(
    @NotBlank(message = "Code is mandatory.")
        @Size(min = 10, max = 10, message = "Code must be 10 characters long.")
        @JsonProperty("code")
        String code,
    String name,
    @NotNull(message = "Price in euros is mandatory.")
        @Min(value = 0, message = "Price cannot be negative.")
        @JsonProperty("price_eur")
        BigDecimal priceEur,
    String description,
    @JsonProperty("is_available") boolean isAvailable) {}
