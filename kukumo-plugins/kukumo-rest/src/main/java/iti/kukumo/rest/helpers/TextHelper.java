/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package iti.kukumo.rest.helpers;


import iti.kukumo.api.datatypes.Assertion;
import org.junit.ComparisonFailure;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import iti.commons.jext.Extension;
import iti.kukumo.rest.ContentTypeHelper;
import iti.kukumo.rest.MatchMode;



@Extension(provider = "iti.kukumo", name = "rest-text-helper", extensionPoint = "iti.kukumo.rest.ContentTypeHelper")
public class TextHelper implements ContentTypeHelper {


    @Override
    public ContentType contentType() {
        return ContentType.TEXT;
    }


    @Override
    public void assertContent(String expected, String actual, MatchMode matchMode) {
        switch (matchMode) {
        case STRICT:
        case STRICT_ANY_ORDER:
            if (!actual.trim().equals(expected.trim())) {
                throw new ComparisonFailure("Text differences", expected, actual);
            }
            break;
        case LOOSE:
            if (!actual.trim().contains(expected.trim())) {
                throw new ComparisonFailure("Text differences", expected, actual);
            }
        }
    }



    @Override
    public <T> void assertFragment(
        String fragment,
        ValidatableResponse response,
        Class<T> dataType,
        Assertion<T> assertion
    ) {
        throw new UnsupportedOperationException("Not implemented for content type "+contentType());
    }


    @Override
    public void assertContentSchema(String expectedSchema, String content) {
        throw new UnsupportedOperationException("Not implemented for content type "+contentType());
    }
}