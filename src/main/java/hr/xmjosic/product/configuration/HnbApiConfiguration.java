package hr.xmjosic.product.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Configuration properties for the HNB API. */
@ConfigurationProperties(prefix = "hnb")
public record HnbApiConfiguration(String url, String path) {}
