package shop.kokodo.memberservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
