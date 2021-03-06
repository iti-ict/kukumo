/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package iti.kukumo.rest.test.mockserver;


import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;



public class TestInMemoryServer {

    private static InMemoryServer server;


    @BeforeClass
    public static void setupServer() throws Exception {
        server = new InMemoryServer(
            HttpServerAdapter.serverFactory(),
            InMemoryServer.Format.XML,
            StandardCharsets.UTF_8,
            8888,
            "src/test/resources/data.json",
            InMemoryServer.Format.JSON
        );
    }


    @Test
    public void testSerializedXML() throws Exception {
        Object resolved = server.resolvePath("/users/user1").pop();
        byte[] serializedBytes = server.serialize(resolved);
        String serializedString = new String(serializedBytes);
        System.out.println(serializedString);
        assertEquals(
            "<data><id>user1</id><name>User One</name><age>11</age><vegetables><id>1</id><description>Cucumber</description></vegetables><vegetables><id>2</id><description>Gherkin</description></vegetables><contact><email>user1@mail</email></contact></data>",
            serializedString
        );
    }


    @AfterClass
    public static void teardownServer() throws IOException {
        server.stop();
    }
}