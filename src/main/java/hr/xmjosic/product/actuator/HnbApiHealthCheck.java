package hr.xmjosic.product.actuator;

import hr.xmjosic.product.dto.HnbRateDto;
import hr.xmjosic.product.service.HnbApiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * It checks the health of the HNB API by retrieving USD exchange rates and constructing a health
 * status based on the availability of the rates.
 */
@Component("hnb_api")
@RequiredArgsConstructor
@Slf4j
public class HnbApiHealthCheck implements HealthIndicator {

  /** The HNB API service for retrieving USD exchange rates. */
  private final HnbApiService hnbApiService;

  /**
   * Overrides the health method to check the HNB API health.
   *
   * @return Health status indicating the health of the HNB API
   */
  @Override
  public Health health() {
    log.info("Checking HNB API health...");
    Health.Builder builder = new Health.Builder();
    List<HnbRateDto> usdHnbRates;
    try {
      usdHnbRates = hnbApiService.getUsdHnbRates();
    } catch (Exception e) {
      return builder.down().withDetail("Error", e.getMessage()).build();
    }
    return CollectionUtils.isEmpty(usdHnbRates)
        ? builder.down().withDetail("Error", "No rates found").build()
        : builder
            .up()
            .withDetail("USD rate", usdHnbRates.stream().findFirst().orElse(null))
            .build();
  }
}
