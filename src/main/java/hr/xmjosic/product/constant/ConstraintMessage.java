package hr.xmjosic.product.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Messages for the {@link ConstraintName} enum. */
@RequiredArgsConstructor
@Getter
public enum ConstraintMessage {

  UNIQUE_CODE_CONSTRAINT(ConstraintName.UNIQUE_CODE_CONSTRAINT, "Product code must be unique");

  /** The name of the constraint. */
  private final String name;

  /** The message associated with the constraint. */
  private final String message;

  /** The values of the enum. */
  private static final ConstraintMessage[] VALUES = values();

  /**
   * Retrieves the message associated with the given constraint name.
   *
   * @param name the name of the constraint
   * @return the message associated with the constraint, or null if not found
   */
  public static String getMessageByConstraintName(String name) {
    for (ConstraintMessage value : VALUES) {
      if (value.getName().equals(name)) {
        return value.getMessage();
      }
    }
    return null;
  }
}
