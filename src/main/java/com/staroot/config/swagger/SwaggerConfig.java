package com.staroot.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


//ref url : http://yookeun.github.io/java/2017/02/26/java-swagger/
@Configuration
@EnableSwagger2
public class SwaggerConfig {
				
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.staroot.controller"))
                .paths(PathSelectors.any())
                .build();

    }
}