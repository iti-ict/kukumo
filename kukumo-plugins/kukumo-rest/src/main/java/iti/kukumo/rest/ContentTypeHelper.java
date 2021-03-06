/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package iti.kukumo.rest;


import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import iti.commons.jext.ExtensionPoint;
import iti.kukumo.api.datatypes.Assertion;
import iti.kukumo.api.plan.Document;



@ExtensionPoint
public interface ContentTypeHelper {

    ContentType contentType();

    void assertContent(String expected, String actual, MatchMode matchMode);


    default void assertContent(
        Document expected,
        ExtractableResponse<Response> response,
        MatchMode matchMode
    ) {
        assertContent(expected.getContent(), response.asString(), matchMode);
    }


    default void assertContent(
        String expected,
        ExtractableResponse<Response> response,
        MatchMode matchMode
    ) {
        assertContent(expected, response.asString(), matchMode);
    }


    <T> void assertFragment(
        String fragment,
        ValidatableResponse response,
        Class<T> dataType,
        Assertion<T> matcher
    );


    void assertContentSchema(String expectedSchema, String content);

}