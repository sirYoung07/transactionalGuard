package com.market.transactionguard.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info =  @Info(
        contact = @Contact(name = "Onadiran Afeez Bayonle", url = "?", email = "onadiranght2019@gmail.com"),
        title = "OpenAI specification: Transactional Guard",
        version = "2.0",
        license = @License(name = "MIT license", url = "https://choosealicense.com/licenses/mit/"),
        termsOfService = "Terms of Service"
    ),

    servers = {
        @Server(description = "local ENV", url = "http://localhost:8000"),
        @Server(description = "prod ENV", url = "https://young.dev")
    },
    security = {@SecurityRequirement(name = "bearerAuth")}
)
@SecurityScheme(name = "bearerAuth", description = "JWT authentication description", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {
}
