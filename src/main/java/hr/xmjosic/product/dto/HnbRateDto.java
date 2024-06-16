package hr.xmjosic.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used to transfer data about HNB exchange rates
 *
 * @param brojTecajnice the number of the exchange rate
 * @param datumPrimjene the date of the exchange rate
 * @param drzava the country
 * @param drzavaIso the ISO code of the country
 * @param kupovniTecaj the buy exchange rate
 * @param prodajniTecaj the sell exchange rate
 * @param sifraValute the currency code
 * @param srednjiTecaj the average exchange rate
 * @param valuta the currency
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record HnbRateDto(
    @JsonProperty("broj_tecajnice") String brojTecajnice,
    @JsonProperty("datum_primjene") String datumPrimjene,
    @JsonProperty("drzava") String drzava,
    @JsonProperty("drzava_iso") String drzavaIso,
    @JsonProperty("kupovni_tecaj") String kupovniTecaj,
    @JsonProperty("prodajni_tecaj") String prodajniTecaj,
    @JsonProperty("sifra_valute") String sifraValute,
    @JsonProperty("srednji_tecaj") String srednjiTecaj,
    @JsonProperty("valuta") String valuta) {}
