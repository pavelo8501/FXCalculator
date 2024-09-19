package lv.fx.calculator.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.tags.Tag
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Primary

@Configuration
class SwaggerConfig {

    @Bean
    @Primary
    fun adminOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Admin API")
                    .description("Administrative API documentation for the FX Calculator application")
                    .version("1.0.0")
            )
    }

    @Bean
    fun publicOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Public API")
                    .description("Public API documentation for the FX Calculator application")
                    .version("1.0.0")
            )
    }

    @Bean
    fun adminApiCustomizer(@Qualifier("adminOpenAPI") openApi: OpenAPI): OpenApiCustomizer {
        return OpenApiCustomizer {
            val paths = Paths()
            openApi.paths?.filter { (key, _) -> key.startsWith("/admin/api") }
                ?.forEach { (key, value) -> paths.addPathItem(key, value) }
            openApi.paths(paths)
        }
    }

    @Bean
    fun publicApiCustomizer(@Qualifier("publicOpenAPI") openApi: OpenAPI): OpenApiCustomizer {
        return OpenApiCustomizer {
            val paths = Paths()
            openApi.paths?.filter { (key, _) -> key.startsWith("/api") && !key.startsWith("/admin/api") }
                ?.forEach { (key, value) -> paths.addPathItem(key, value) }
            openApi.paths(paths)
        }
    }

}