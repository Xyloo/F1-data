package pl.pollub.f1data.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for OpenAPI and Swagger
 */
@Configuration
public class OpenAPIConfig {

    @Value("${f1data.openapi.dev-url}")
    private String devUrl;

    @Value("${f1data.openapi.prod-url}")
    private String prodUrl;

    //Swagger UI URL: http://localhost:8080/swagger-ui/index.html
    //Swagger API URL: http://localhost:8080/v3/api-docs

    /**
     * This method configures OpenAPI and Swagger.
     * This is used by Spring to generate OpenAPI specification.
     * @see <a href="https://swagger.io/docs/specification/about/">Swagger Specification</a>
     * @return OpenAPI object
     */
    @Bean
    public OpenAPI f1OpenAPIConfig() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("s95408@pollub.pl");
        contact.setName("Tomasz Gęszka & Cezary Gozdalski");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("F1Data API")
                .version("1.0")
                .contact(contact)
                .description("API for F1Data application")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}