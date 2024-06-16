package hr.xmjosic.product.exception;

/** Exception thrown when the HNB API returns an error. */
public class HnbApiException extends RuntimeException {

  public HnbApiException(String message) {
    super(message);
  }

  public HnbApiException(String message, Throwable cause) {
    super(message, cause);
  }
}
