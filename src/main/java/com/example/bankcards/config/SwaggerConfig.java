package com.example.bankcards.config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank Cards API")
                        .description("REST API системы управления банковскими картами.\n" +
                                     "\n" +
                                     "    Функциональность:\n" +
                                     "    - регистрация и аутентификация пользователей\n" +
                                     "    - управление банковскими картами\n" +
                                     "    - переводы между картами\n" +
                                     "    - заявки на активацию и блокировку карт\n" +
                                     "    - разграничение доступа по ролям (ADMIN / USER)\n" +
                                     "\n" +
                                     "    Аутентификация осуществляется с помощью JWT (Bearer Token).")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
