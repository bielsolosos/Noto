package space.bielsolososdev.noto.infrastructure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

@Validated
@Configuration
@ConfigurationProperties(prefix = "noto")
class NotoProperties {

    var jwt: Jwt = Jwt()
    var registrationEnabled: Boolean = false

    class Jwt {
        var secret: String = ""
        var expiration: Long = 3600000
        var refreshExpiration: Long = 86400000
    }
}

