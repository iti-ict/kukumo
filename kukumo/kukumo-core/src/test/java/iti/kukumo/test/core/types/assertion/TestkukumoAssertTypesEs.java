/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package iti.kukumo.test.core.types.assertion;


import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import iti.kukumo.api.KukumoDataType;
import iti.kukumo.api.datatypes.Assertion;
import iti.kukumo.core.datatypes.assertion.KukumoAssertTypes;


@SuppressWarnings("rawtypes")
public class TestkukumoAssertTypesEs {

    private final Locale locale = Locale.forLanguageTag("es");


    @Test
    public void testInteger() throws ParseException {

        KukumoDataType<Assertion> intAssertion = KukumoAssertTypes
            .binaryNumberAssert("int-assert", false, Number::intValue);
        Map<String, Object> exp = new LinkedHashMap<>();
        exp.put("es -7", -7);
        exp.put("es igual a 8.000", 8000);
        exp.put("es mayor que 11", 12);
        exp.put("es mayor o igual que 12", 12);
        exp.put("es menor que 13", 12);
        exp.put("es menor o igual que 13", 13);
        exp.put("no es -7", -8);
        exp.put("no es igual a 8.000", 8001);
        exp.put("no es mayor que 11", 11);
        exp.put("no es mayor o igual que 12", 10);
        exp.put("no es menor que 13", 13);
        exp.put("no es menor o igual que 13", 15);

        for (Entry<String, Object> e : exp.entrySet()) {
            Assertion<?> matcher = intAssertion.parse(locale, e.getKey());
            assertThat(matcher).as("null assertion for: " + e.getKey()).isNotNull();
            assertThat(matcher.test(e.getValue()))
                .as("failed match for: " + e.getKey() + " with " + e.getValue()).isTrue();
        }

        intAssertion.parse(locale, "es nulo").test(null);
        intAssertion.parse(locale, "no es nulo").test(7);

    }


    @Test
    public void testLong() throws ParseException {

        KukumoDataType<Assertion> longAssertion = KukumoAssertTypes
            .binaryNumberAssert("long-assert", false, Number::longValue);
        Map<String, Object> exp = new LinkedHashMap<>();
        exp.put("es -7", -7L);
        exp.put("es igual a 8.000", 8000L);
        exp.put("es mayor que 11", 12L);
        exp.put("es mayor o igual que 12", 12L);
        exp.put("es menor que 13", 12L);
        exp.put("es menor o igual que 13", 13L);
        exp.put("no es -7", -8L);
        exp.put("no es igual a 8.000", 8001L);
        exp.put("no es mayor que 11", 11L);
        exp.put("no es mayor o igual que 12", 10L);
        exp.put("no es menor que 13", 13L);
        exp.put("no es menor o igual que 13", 15L);

        for (Entry<String, Object> e : exp.entrySet()) {
            Assertion<?> matcher = longAssertion.parse(locale, e.getKey());
            assertThat(matcher).as("null assertion for: " + e.getKey()).isNotNull();
            assertThat(matcher.test(e.getValue()))
                .as("failed match for: " + e.getKey() + " with " + e.getValue()).isTrue();
        }

        longAssertion.parse(locale, "es nulo").test(null);
        longAssertion.parse(locale, "no es nulo").test(7L);

    }


    @Test
    public void testDouble() throws ParseException {

        KukumoDataType<Assertion> doubleAssertion = KukumoAssertTypes
            .binaryNumberAssert("double-assert", true, Number::doubleValue);
        Map<String, Object> exp = new LinkedHashMap<>();
        exp.put("es -7,0", -7.0);
        exp.put("es igual a 8,0", 8.0);
        exp.put("no es 9,0", 8.0);
        exp.put("no es igual a 10,0", 9.0);
        exp.put("es mayor que 11,0", 12.0);
        exp.put("es mayor o igual que 12,0", 12.0);
        exp.put("es menor que 13,0", 12.0);
        exp.put("es menor o igual que 13,0", 13.0);

        for (Entry<String, Object> e : exp.entrySet()) {
            Assertion<?> matcher = doubleAssertion.parse(locale, e.getKey());
            assertThat(matcher).as("null assertion for: " + e.getKey()).isNotNull();
            assertThat(matcher.test(e.getValue()))
                .as("failed match for: " + e.getKey() + " with " + e.getValue()).isTrue();
        }
    }


    @Test
    public void testBigDecimal() throws ParseException {

        KukumoDataType<Assertion> bigDecimalAssertion = KukumoAssertTypes
            .binaryBigDecimalAssert("bigdecimal-assert", true, x -> x);
        Map<String, Object> exp = new LinkedHashMap<>();
        exp.put("es -7,0", BigDecimal.valueOf(7.0));
        exp.put("es igual a 8,0", BigDecimal.valueOf(7.0));
        exp.put("no es 9,0", BigDecimal.valueOf(9.0));
        exp.put("no es igual a 10,0", BigDecimal.valueOf(10.0));
        exp.put("es mayor que 11,0", BigDecimal.valueOf(10.0));
        exp.put("es mayor o igual que 12,0", BigDecimal.valueOf(10.0));
        exp.put("es menor que 13,0", BigDecimal.valueOf(15.0));
        exp.put("es menor o igual que 13,0", BigDecimal.valueOf(15.0));

        for (Entry<String, Object> e : exp.entrySet()) {
            Assertion<?> matcher = bigDecimalAssertion.parse(locale, e.getKey());
            assertThat(matcher).as("null assertion for: " + e.getKey()).isNotNull();
            assertThat(matcher.test(e.getValue()))
                .as("failed negative match for: " + e.getKey() + " with " + e.getValue()).isFalse();
        }
    }

}