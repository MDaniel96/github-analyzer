package bme.softwarearchitectures.githubanalyzerserver.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {

    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration().apply {
            allowCredentials = true
            allowedOrigins = listOf("*")
            allowedHeaders = listOf("Origin", "Content-Type", "Accept")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        }
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}