package fi.develon.ev.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author mahmood
 * @since 9/10/21
 */
@Configuration
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Data
@NoArgsConstructor
public class ApplicationProperties {

}
