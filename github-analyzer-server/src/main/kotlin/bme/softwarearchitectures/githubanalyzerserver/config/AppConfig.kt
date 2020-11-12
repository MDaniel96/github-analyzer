package bme.softwarearchitectures.githubanalyzerserver.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
@PropertySource(value = ["classpath:default.properties"])
class AppConfig {

    @Value("\${access.token}")
    val accessToken = ""
}