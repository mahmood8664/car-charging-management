package fi.develon.ev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author mahmood
 * @since 9/10/21
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean(name = "swaggerSpringfoxApiDocket")
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.basePackage("fi.develon"))
                .build()
                .useDefaultResponseMessages(false);
    }

}
