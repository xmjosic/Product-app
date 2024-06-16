package hr.xmjosic.product.configuration;

import hr.xmjosic.product.exception.HnbApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

/** Configuration for the HNB API RestClient. */
@Configuration
@EnableConfigurationProperties(HnbApiConfiguration.class)
@RequiredArgsConstructor
@Slf4j
public class RestClientConfiguration {

  /** Configuration properties for the HNB API */
  private final HnbApiConfiguration hnbApiConfig;

  /**
   * Creates and configures the HNB API RestClient with the specified base URL and default status
   * handler for client and server error HTTP codes.
   *
   * @return the configured RestClient for the HNB API
   */
  @Bean("hnbApiRestClient")
  public RestClient restClient() {
    log.info(
        "Creating HNB API RestClient with base URL: {} and default status handler for client and server error HTTP codes",
        hnbApiConfig.url());
    return RestClient.builder()
        .baseUrl(hnbApiConfig.url())
        .defaultStatusHandler(
            HttpStatusCode::isError,
            (request, response) -> {
              log.error(
                  "Error while requesting HNB API: {} {}",
                  response.getStatusCode(),
                  response.getStatusText());
              throw new HnbApiException(response.getStatusText());
            })
        .build();
  }
}
