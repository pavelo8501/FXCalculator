package lv.fx.calculator.config

import io.swagger.v3.core.converter.ModelConverters
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.tags.Tag
import org.springdoc.core.converters.WebFluxSupportConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary

@Configuration
class SwaggerConfig {

    //Admin API documentation
    @Bean
    fun adminApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("admin")
            .pathsToMatch("/admin/api/**")
            .pathsToExclude("/public/**")
            .displayName("Admin API")
            .addOpenApiCustomizer {
                it.info.title = "Admin API Documentation"
                it.info.description = "Admin API for managing conversion fees and rates."
            }
            .build()
    }

    // Public API documentation setup found at /api/swagger-ui/
    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("public")
            .pathsToMatch("/api/**")
            .displayName("Public API")
            .addOpenApiCustomizer {
                it.info.title = "Public API Documentation"
                it.info.description = "Public API for currency conversion."
            }
            .build()
    }

}