package lv.fx.calculator.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.tags.Tag
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary

@Configuration
class SwaggerConfig {

    // Public API documentation at /admin-docs
//    @Bean
//    fun adminApi(): GroupedOpenApi {
//        return GroupedOpenApi.builder()
//            .group("admin")
//            .pathsToMatch("/admin/api/**")
//            .addOpenApiCustomizer {
//                it.info.title = "Admin API Documentation"
//                it.info.description = "Admin API for managing conversion fees and rates."
//            }
//            .build()
//    }
//
//    // Public API documentation at /public-docs
//    @Bean
//    fun publicApi(): GroupedOpenApi {
//        return GroupedOpenApi.builder()
//            .group("public")
//            .pathsToMatch("/api/**")
//            .addOpenApiCustomizer {
//                it.info.title = "Public API Documentation"
//                it.info.description = "Public API for currency conversion."
//            }
//            .build()
//    }

    @Bean
    fun customOpenAPI(
        @Value("admin") appDescription: String?, @Value(
            "1"
        ) appVersion: String?
    ): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Microservice Base Service Application API")
                    .version(appVersion)
                    .description(appDescription)
                    .termsOfService("http://swagger.io/terms/")
            )
    }

}