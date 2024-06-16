package hr.xmjosic.product.model;

import hr.xmjosic.product.constant.ConstraintName;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/** Represents a product */
@Getter
@Setter
@Entity(name = "Product")
@Table(
    name = "product",
    uniqueConstraints =
        @UniqueConstraint(name = ConstraintName.UNIQUE_CODE_CONSTRAINT, columnNames = "code"))
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Size(min = 10, max = 10)
  @Column(name = "code", nullable = false, length = 10)
  private String code;

  @Column(name = "name")
  private String name;

  @Min(value = 0)
  @Column(name = "price_eur")
  private BigDecimal priceEur;

  @Min(value = 0)
  @Column(name = "price_usd")
  private BigDecimal priceUsd;

  @Column(name = "description")
  private String description;

  @Column(name = "is_available")
  private boolean isAvailable;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return Objects.equals(id, product.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Product{"
        + "id="
        + id
        + ", code='"
        + code
        + '\''
        + ", name='"
        + name
        + '\''
        + ", priceEur="
        + priceEur
        + ", priceUsd="
        + priceUsd
        + ", description='"
        + description
        + '\''
        + ", isAvailable="
        + isAvailable
        + '}';
  }
}
