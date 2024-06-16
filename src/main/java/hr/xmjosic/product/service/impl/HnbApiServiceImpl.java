package hr.xmjosic.product.service.impl;

import hr.xmjosic.product.configuration.HnbApiConfiguration;
import hr.xmjosic.product.dto.HnbRateDto;
import hr.xmjosic.product.exception.HnbApiException;
import hr.xmjosic.product.service.HnbApiService;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;

/** Implementation of {@link HnbApiService}. */
@Service
@RequiredArgsConstructor
@Slf4j
public class HnbApiServiceImpl implements HnbApiService {

  /** HNB API parameter for currency. */
  public static final String VALUTA_PARAM = "valuta";

  /** HNB API parameter for currency. */
  public static final String USD = "USD";

  /** Locale language code for Croatia. */
  public static final String HR = "hr";

  /** RestClient for HNB API. */
  private final RestClient hnbApiRestClient;

  /** Configuration for HNB API. */
  private final HnbApiConfiguration hnbApiConfig;

  @Override
  public BigDecimal getUsdSellingRate() {
    log.debug("Retrieving USD selling rate from HNB API");
    List<HnbRateDto> response = getUsdHnbRates();
    if (CollectionUtils.isEmpty(response)) throw new HnbApiException("No response from HNB API.");
    return response.stream()
        .findFirst()
        .map(HnbRateDto::prodajniTecaj)
        .map(
            s -> {
              try {
                return new BigDecimal(
                    DecimalFormat.getNumberInstance(Locale.forLanguageTag(HR)).parse(s).toString());
              } catch (ParseException e) {
                throw new HnbApiException("Could not parse HNB API response.", e);
              }
            })
        .orElseThrow(() -> new NoSuchElementException("Could not find USD selling rate."));
  }

  @Override
  public List<HnbRateDto> getUsdHnbRates() {
    log.debug("Retrieving USD HNB rates from HNB API");
    return hnbApiRestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder.path(hnbApiConfig.path()).queryParam(VALUTA_PARAM, USD).build())
        .retrieve()
        .body(new ParameterizedTypeReference<>() {});
  }
}
