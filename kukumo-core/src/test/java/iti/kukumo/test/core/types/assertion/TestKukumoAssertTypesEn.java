package iti.kukumo.test.core.types.assertion;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.hamcrest.Matcher;
import org.junit.Test;

import iti.kukumo.api.KukumoDataType;
import iti.kukumo.core.datatypes.assertion.KukumoAssertTypes;

public class TestKukumoAssertTypesEn {

    private Locale locale = Locale.ENGLISH;


    @Test
    public void testInteger() throws ParseException {

        KukumoDataType<Matcher> intMatcher = KukumoAssertTypes.binaryNumbeAssert("int-assert", false, Number::intValue);
        Map<String,Object> exp = new LinkedHashMap<>();
        exp.put("is -7", -7);
        exp.put("is equals to 8,000", 8000);
        exp.put("is greater than 11", 12);
        exp.put("is greater than or equals to 12",12);
        exp.put("is less than 13", 12);
        exp.put("is less than or equals to 13",13);
        exp.put("is not -7", -8);
        exp.put("is not equals to 8,000", 8001);
        exp.put("is not greater than 11", 11);
        exp.put("is not greater than or equals to 12",10);
        exp.put("is not less than 13", 13);
        exp.put("is not less than or equals to 13",15);

        for (Entry<String, Object> e : exp.entrySet()) {
            Matcher<?> matcher = intMatcher.parse(locale, e.getKey());
            assertThat(matcher).as("null assertion for: "+e.getKey()).isNotNull();
            assertThat(matcher.matches(e.getValue())).as("failed match for: "+e.getKey()+" with "+e.getValue()).isTrue();
        }

        intMatcher.parse(locale,"is null").matches(null);
        intMatcher.parse(locale,"is not null").matches(7);

    }





    @Test
    public void testLong() throws ParseException {

        KukumoDataType<Matcher> longMatcher = KukumoAssertTypes.binaryNumbeAssert("long-assert", false, Number::longValue);
        Map<String,Object> exp = new LinkedHashMap<>();
        exp.put("is -7", -7L);
        exp.put("is equals to 8,000", 8000L);
        exp.put("is greater than 11", 12L);
        exp.put("is greater than or equals to 12",12L);
        exp.put("is less than 13", 12L);
        exp.put("is less than or equals to 13",13L);
        exp.put("is not -7", -8L);
        exp.put("is not equals to 8,000", 8001L);
        exp.put("is not greater than 11", 11L);
        exp.put("is not greater than or equals to 12",10L);
        exp.put("is not less than 13", 13L);
        exp.put("is not less than or equals to 13",15L);

        for (Entry<String, Object> e : exp.entrySet()) {
            Matcher<?> matcher = longMatcher.parse(locale, e.getKey());
            assertThat(matcher).as("null assertion for: "+e.getKey()).isNotNull();
            assertThat(matcher.matches(e.getValue())).as("failed match for: "+e.getKey()+" with "+e.getValue()).isTrue();
        }

        longMatcher.parse(locale,"is null").matches(null);
        longMatcher.parse(locale,"is not null").matches(7L);

    }


    @Test
    public void testDouble() throws ParseException {
        KukumoDataType<Matcher> doubleMatcher = KukumoAssertTypes.binaryNumbeAssert("double-assert", true, Number::doubleValue);
        Map<String,Object> exp = new LinkedHashMap<>();
        exp.put("is -7.0", -7.0);
        exp.put("is equals to 8.0", 8.0);
        exp.put("is not 9.0", 8.0);
        exp.put("is not equals to 10.0", 9.0);
        exp.put("is greater than 11.0", 12.0);
        exp.put("is greater than or equals to 12.0",12.0);
        exp.put("is less than 13.0", 12.0);
        exp.put("is less than or equals to 13.0",13.0);

        for (Entry<String, Object> e : exp.entrySet()) {
            Matcher<?> matcher = doubleMatcher.parse(locale, e.getKey());
            assertThat(matcher).as("null assertion for: "+e.getKey()).isNotNull();
            assertThat(matcher.matches(e.getValue())).as("failed match for: "+e.getKey()+" with "+e.getValue()).isTrue();
        }
    }




    @Test
    public void testBigDecimal() throws ParseException {
        KukumoDataType<Matcher> bigDecimalMatcher = KukumoAssertTypes.binaryBigDecimalAssert("bigdecimal-assert", true, x->x);
        Map<String,Object> exp = new LinkedHashMap<>();
        exp.put("is -7.0", BigDecimal.valueOf(7.0));
        exp.put("is equals to 8.0", BigDecimal.valueOf(7.0));
        exp.put("is not 9.0", BigDecimal.valueOf(9.0));
        exp.put("is not equals to 10.0", BigDecimal.valueOf(10.0));
        exp.put("is greater than 11.0", BigDecimal.valueOf(10.0));
        exp.put("is greater than or equals to 12.0",BigDecimal.valueOf(10.0));
        exp.put("is less than 13.0", BigDecimal.valueOf(15.0));
        exp.put("is less than or equals to 13.0",BigDecimal.valueOf(15.0));

        for (Entry<String, Object> e : exp.entrySet()) {
            Matcher<?> matcher = bigDecimalMatcher.parse(locale,e.getKey());
            assertThat(matcher).as("null assertion for: "+e.getKey()).isNotNull();
            assertThat(matcher.matches(e.getValue())).as("failed negative match for: "+e.getKey()+" with "+e.getValue()).isFalse();
        }
    }


}