package iti.kukumo.core.datatypes;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import iti.kukumo.util.ThrowableFunction;

public class KukumoNumberDataType<T> extends KukumoDataTypeBase<T> {


    public static <T> KukumoNumberDataType<T> createFromNumber(String name, Class<T> javaType, boolean includeDecimals, ThrowableFunction<Number,T> converter) {
        return new KukumoNumberDataType<>(name,javaType,includeDecimals,false,converter);
    }

    public static <T> KukumoNumberDataType<T> createFromBigDecimal(String name, Class<T> javaType, boolean includeDecimals, ThrowableFunction<BigDecimal,T> converter) {
        return new KukumoNumberDataType<>(name,javaType,includeDecimals,true, castConverter(BigDecimal.class::cast,converter));
    }


    protected KukumoNumberDataType(String name, Class<T> javaType, boolean includeDecimals, boolean useBigDecimal, ThrowableFunction<Number,T> converter) {
        super(name, javaType,
                locale -> numericRegexPattern(locale, includeDecimals),
                locale -> decimalFormat(locale,useBigDecimal).toLocalizedPattern(),
                locale -> parser(locale, includeDecimals, converter)
                );
    }



    public static <T> ThrowableFunction<Number,T> castConverter(ThrowableFunction<Number,BigDecimal> caster, ThrowableFunction<BigDecimal,T> converter) {
        return number -> converter.apply(caster.apply(number));
    }


    public static <T> TypeParser<T> parser(Locale locale, boolean includeDecimals, ThrowableFunction<Number,T> converter) {
        final ThrowableFunction<String,Number> parser = source -> decimalFormat(locale, includeDecimals).parse(source);
        return TypeParser.from(parser.andThen(converter));
    }



    public static DecimalFormat decimalFormat(Locale locale, boolean useBigDecimal) {
        DecimalFormat format;
        if (useBigDecimal) {
            format = (DecimalFormat) DecimalFormat.getNumberInstance(locale);
            format.setParseBigDecimal(true);
        } else {
            format = (DecimalFormat) DecimalFormat.getIntegerInstance(locale);
        }
        return format;
    }



    public static String numericRegexPattern(Locale locale, boolean includeDecimals) {
        final DecimalFormat format = decimalFormat(locale, includeDecimals);
        final DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        final StringBuilder pattern = new StringBuilder("-?");
        pattern
        .append("\\d{1," + format.getGroupingSize() + "}")
        .append("(\\" + symbols.getGroupingSeparator()+"?")
        .append("\\d{1," + format.getGroupingSize() + "})*");
        if (includeDecimals) {
            pattern.append("\\" + symbols.getDecimalSeparator()).append("\\d+?");
        }
        return pattern.toString();
    }
}