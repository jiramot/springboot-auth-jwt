package com.jiramot.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
@Import({SpringDataRestConfiguration.class})
public class SwaggerConfig {
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .globalOperationParameters(newArrayList(new ParameterBuilder()
            .name("Authorization")
            .description("token")
            .modelRef(new ModelRef("string"))
            .parameterType("header")
            .required(false)
            .build()))
        .groupName("cashnow-mobile-api")
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.jiramot.auth"))
        .build();

  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder().title("Cashnow Mobile Edge API")
        .description("Cashnow Mobile Edge API")
        .contact(new Contact("Watchanon Numnam", "", "jiramot@gmail.com"))
        .version("1.0")
        .build();
  }

}