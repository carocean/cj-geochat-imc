package cj.geochat.imc.comet.config;

import cj.geochat.ability.feign.annotation.EnableCjFeign;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableCjFeign
@EnableFeignClients(basePackages = "cj.geochat.imc.comet.remote")
@Configuration
public class OpenFeignConfig {
}
