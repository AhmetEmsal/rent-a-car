package kodlama.io.rentacar.core.configuration.converters;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.sql.Timestamp;
import java.time.LocalDateTime;
public class TimestampToLocalDateTimeConverter implements Converter<Timestamp, LocalDateTime> {
    @Override
    public LocalDateTime convert(MappingContext<Timestamp, LocalDateTime> context) {
        Timestamp timestamp = context.getSource();
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}