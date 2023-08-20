package cj.geochat.imc.postbox.config;

import cj.geochat.ability.feign.annotation.EnableCjFeign;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableCjFeign
@EnableFeignClients(basePackages = "cj.geochat.imc.postbox.remote")
@Configuration
public class OpenFeignConfig {
}
