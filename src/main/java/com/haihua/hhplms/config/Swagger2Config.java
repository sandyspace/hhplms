package com.haihua.hhplms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket createPmRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("促销管理")
                //.genericModelSubstitutes(ResultBean.Success.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .paths(or(regex("/api/pm/.*")))
                .build()
                .apiInfo(pmApiInfo())
                .globalOperationParameters(setHeaderToken());
    }

    private ApiInfo pmApiInfo() {
        return new ApiInfoBuilder()
                .title("促销管理")
                .description("促销管理")
                .contact(new Contact("ShanCheng", null, "shanc@133.cn"))
                .version("1.0")
                .build();
    }

    @Bean
    public Docket createSysRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("系统管理")
                //.genericModelSubstitutes(ResultBean.Success.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .paths(or(regex("/api/sys/.*")))
                .build()
                .apiInfo(sysApiInfo())
                .globalOperationParameters(setHeaderToken());
    }

    private ApiInfo sysApiInfo() {
        return new ApiInfoBuilder()
                .title("系统管理")
                .description("系统管理")
                .contact(new Contact("ShanCheng", null, "shanc@133.cn"))
                .version("1.0")
                .build();
    }

    @Bean
    public Docket createAnaRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("认证与鉴权")
                //.genericModelSubstitutes(ResultBean.Success.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .paths(or(regex("/api/auth/.*"), regex("/api/ana/.*")))
                .build()
                .apiInfo(anaApiInfo())
                .globalOperationParameters(setHeaderToken());
    }

    private ApiInfo anaApiInfo() {
        return new ApiInfoBuilder()
                .title("认证与鉴权")
                .description("认证与鉴权")
                .contact(new Contact("ShanCheng", null, "shanc@133.cn"))
                .version("1.0")
                .build();
    }

    private List<Parameter> setHeaderToken() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("Authorization").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return pars;
    }
}
