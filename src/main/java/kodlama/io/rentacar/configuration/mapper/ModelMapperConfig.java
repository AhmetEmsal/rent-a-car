package kodlama.io.rentacar.configuration.mapper;

import kodlama.io.rentacar.configuration.converters.TimestampToLocalDateTimeConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(new TimestampToLocalDateTimeConverter());
        return new ModelMapper();
    }
}