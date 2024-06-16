package hr.xmjosic.product.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** The names of the constraints. */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstraintName {

  /** The name of the unique code constraint. */
  public static final String UNIQUE_CODE_CONSTRAINT = "product_unique_code_constraint";
}
