package kodlama.io.rentacar.configuration.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.Date;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<Timestamp, Date> timestampToDateConverter = new Converter<Timestamp, Date>() {
            @Override
            public Date convert(MappingContext<Timestamp, Date> context) {
                return new Date(context.getSource().getTime());
            }
        };

        modelMapper.addConverter(timestampToDateConverter);
        return new ModelMapper();
    }
}
