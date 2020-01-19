package iti.commons.gherkin.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class ResourceLoader {

    public static InputStream openInputStream(Class<?> type, String resource) throws IOException {
        var inputStream = type.getClassLoader().getResourceAsStream(resource);
        if (inputStream == null) {
            inputStream = type.getModule().getResourceAsStream(resource);
        }
        if (inputStream == null) {
            throw new IOException("cannot find neither class nor module resource "+resource);
        }
        return inputStream;
    }


    public static Reader openReader(Class<?> type, String resource) throws IOException {
        return new InputStreamReader(openInputStream(type,resource),StandardCharsets.UTF_8);
    }

}
