package Choi.clean_lottery.web.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.IntStream;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }

        IntStream mapToInt = Arrays.stream(source.split("-")).mapToInt(x -> Integer.parseInt(x));
        int[] dates = mapToInt.toArray();
        LocalDate date = LocalDate.of(dates[0], dates[1], dates[2]);
        return date;
    }
}
