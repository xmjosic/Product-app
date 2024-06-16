package hr.xmjosic.product.service;

import hr.xmjosic.product.dto.HnbRateDto;
import hr.xmjosic.product.exception.HnbApiException;
import java.math.BigDecimal;
import java.util.List;

/** Service for HNB API. */
public interface HnbApiService {

  /**
   * Retrieves the USD selling rate from the HNB API.
   *
   * @return a BigDecimal representing the USD selling rate
   * @throws HnbApiException if there is an error retrieving the USD selling rate from the HNB API
   */
  BigDecimal getUsdSellingRate();

  /**
   * Retrieves a list of HnbRateDto objects representing the USD HNB rates.
   *
   * @return a list of HnbRateDto objects representing the USD HNB rates
   * @throws HnbApiException if there is an error retrieving the USD HNB rates from the HNB API
   */
  List<HnbRateDto> getUsdHnbRates();
}
