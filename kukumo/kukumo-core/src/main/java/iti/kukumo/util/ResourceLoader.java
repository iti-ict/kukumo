/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package iti.kukumo.util;


import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;

import iti.kukumo.api.Kukumo;
import iti.kukumo.api.KukumoException;
import iti.kukumo.api.Resource;
import iti.kukumo.api.extensions.ResourceType;


public class ResourceLoader {

    private static final String CLASSPATH_PROTOCOL = "classpath:";
    private static final String FILE_PROTOCOL = "file";
    private static final File APPLICATION_FOLDER = new File(System.getProperty("user.dir"));
    private static final Logger LOGGER = Kukumo.LOGGER;
    private static final int BUFFER_SIZE = 2048;



    public interface Parser<T> {
        T parse(InputStream stream, Charset charset) throws IOException;
    }


    private final Charset charset;


    public ResourceLoader(Charset charset) {
        this.charset = charset;
    }


    public ResourceLoader() {
        this.charset = StandardCharsets.UTF_8;
        Locale.setDefault(Locale.ENGLISH); // avoid different behaviours regarding the OS language
    }




    public <T> Resource<T> fromInputStream(ResourceType<T> resourceType, InputStream inputStream) {
        try {
            return new Resource<T>("", "", resourceType.parse(inputStream,charset));
        } catch (IOException e) {
            throw new KukumoException("Error reading input stream: ",e);
        }
    }




    public Reader reader(URL url) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            byte[] bytes = toByteArray(inputStream);
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer resourceBuffer = decoder.decode(ByteBuffer.wrap(bytes));
            return new CharArrayReader(resourceBuffer.array());
        } catch (CharacterCodingException e) {
            LOGGER.error(
                "ERROR CHECKING CHARSET {} IN RESOURCE {uri} : {error}",
                charset,
                url
            );
            throw e;
        }
    }





    public String readFileAsString(File file) {
        return readFileAsString(file, StandardCharsets.UTF_8);
    }


    public String readFileAsString(File file, Charset charset) {
        try {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                return toString(inputStream, charset);
            }
        } catch (IOException e) {
            throw new KukumoException("Error reading text file {} : {}", file, e.getMessage(), e);
        }
    }




    /**
     * Creates a new reader based on the received path:
     * <li>If starts with <tt>classpath:</tt> it will try to locate the resource
     * in the classpath</li>
     * <li>If starts with <tt>http:</tt> it will try to download the resource
     * from the web</li>
     * <li>If starts with <tt>file:</tt> it will try to locate the resource in
     * the filesystem with the absolute path</li>
     * <li>Otherwise, it will try to locate the resource in the filesystem from
     * the application directory</li>
     * <p>
     * The obtained reader is not automatically managed, it should be closed
     * manually after using it.
     * </p>
     *
     * @param path
     * @return A new reader
     * @throws IOException
     */
    public Reader reader(String path) throws IOException {
        if (path.startsWith(CLASSPATH_PROTOCOL)) {
            URL url = Thread.currentThread().getContextClassLoader()
                .getResource(path.replace(CLASSPATH_PROTOCOL, ""));
            return reader(url);
        } else {
            return reader(URI.create(path).toURL());
        }
    }


    /**
     * Obtains a resource bundle according the name and locale specified.
     * <p>
     * This method differs from {@link ResourceBundle#getBundle(String, Locale)}
     * in two aspects:
     * <li>The content will be read using the charset defined in the resource
     * loader instance</li>
     * <li>If there is more than one resource available (e.g. a plugin redefines
     * an existing property file), the resource bundle will contain the
     * composition of values.</li>
     * </p>
     *
     * @param resourceBundle
     * @param locale
     * @return
     */
    public ResourceBundle resourceBundle(String resourceBundle, Locale locale) {
        return ResourceBundle.getBundle(resourceBundle, locale);
    }



    public String readResourceAsString(String path) {
        return discoverResources(Arrays.asList(path), x -> true, this::toString).get(0).content()
            .toString();
    }




    public <T> List<Resource<?>> discoverResources(
        List<String> paths,
        ResourceType<T> resourceType
    ) {
        if (LOGGER.isInfoEnabled()) {
            List<Path> absolutePaths = paths.stream().map(Paths::get).map(Path::toAbsolutePath)
                .collect(Collectors.toList());
            LOGGER.info(
                "Discovering resources of type {resourceType} in paths: {uri} ...",
                resourceType.extensionMetadata().name(),
                absolutePaths
            );
        }
        return discoverResources(paths, resourceType::acceptsFilename, resourceType::parse);
    }


    public <T> List<Resource<?>> discoverResources(String path, ResourceType<T> resourceType) {
        return discoverResources(path, resourceType::acceptsFilename, resourceType::parse);
    }


    public <T> List<Resource<?>> discoverResources(
        List<String> paths,
        Predicate<String> filenameFilter,
        Parser<T> parser
    ) {
        List<Resource<?>> discovered = new ArrayList<>();
        for (String path : paths) {
            discovered.addAll(discoverResources(path, filenameFilter, parser));
        }
        if (LOGGER.isInfoEnabled()) {
            discovered.forEach(
                resource -> LOGGER.info(
                    "Discovered resource {uri}",
                    resource.absolutePath()
                )
            );
        }
        return discovered;
    }


    public <T> List<Resource<?>> discoverResources(
        String path,
        Predicate<String> filenameFilter,
        Parser<T> parser
    ) {
        if (path.endsWith("/") || path.endsWith("\\")) {
            path = path.substring(0, path.length() - 1);
        }
        List<Resource<?>> discovered = new ArrayList<>();
        try {
            if (path.startsWith(CLASSPATH_PROTOCOL)) {
                String classPath = path.replace(CLASSPATH_PROTOCOL, "");
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                String absoluteClassPath = classLoaderFolder(classLoader) + classPath;
                Enumeration<URL> urls = loadFromClasspath(path, classLoader);
                while (urls.hasMoreElements()) {
                    discoverResourcesInURL(
                        absoluteClassPath,
                        urls.nextElement(),
                        filenameFilter,
                        parser,
                        discovered
                    );
                }
            } else {
                URL url;
                if (Paths.get(path).isAbsolute()) {
                    url = Paths.get(path).toUri().toURL();
                } else {
                    url = new File(APPLICATION_FOLDER, path).toURI().toURL();
                }
                discoverResourcesInURL(path, url, filenameFilter, parser, discovered);
            }
        } catch (IOException e) {
            LOGGER.debug("{!error} Error discovering resource: {}", e.getMessage(), e);
        }
        return discovered;
    }


    protected <T> void discoverResourcesInURL(
        String startPath,
        URL url,
        Predicate<String> filenameFilter,
        Parser<T> parser,
        List<Resource<?>> discovered
    ) {
        if (FILE_PROTOCOL.equals(url.getProtocol())) {
            try {
                discoverResouresInFile(
                    startPath,
                    new File(url.toURI()),
                    filenameFilter,
                    parser,
                    discovered
                );
            } catch (URISyntaxException e) {
                LOGGER.debug("{!error} Error discovering resource: {}", e.getMessage(), e);
            }
        } else {
            try {
                discovered.add(
                    new Resource<>(
                        url.toString(), url.toString(), parser.parse(url.openStream(), charset)
                    )
                );
            } catch (IOException e) {
                LOGGER.debug("{!error} Error discovering resource: {}", e.getMessage(), e);
            }
        }
    }


    protected <T> void discoverResouresInFile(
        String startPath,
        File file,
        Predicate<String> filenameFilter,
        Parser<T> parser,
        List<Resource<?>> discovered
    ) {
        if (file.isDirectory() && file.listFiles() != null) {
            for (File child : file.listFiles()) {
                discoverResouresInFile(startPath, child, filenameFilter, parser, discovered);
            }
        } else if (file.getName().endsWith(".zip") || file.getName().endsWith(".ZIP")) {
            discoverResourcesInZipFile(startPath, file, filenameFilter, parser, discovered);

        } else if (filenameFilter.test(file.getName())) {
            try (InputStream stream = new FileInputStream(file)) {
                discovered.add(
                    new Resource<>(
                        "file:" + file.getAbsolutePath(),
                        relative(startPath, file.getAbsolutePath()),
                        parser.parse(stream, charset)
                    )
                );
            } catch (IOException e) {
                LOGGER.error(e.toString(), e);
            }
        }

    }


    private <T> void discoverResourcesInZipFile(
        String startPath,
        File file,
        Predicate<String> filenameFilter,
        Parser<T> parser,
        List<Resource<?>> discovered
    ) {
        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
            while (zipEntries.hasMoreElements()) {
                ZipEntry zipEntry = zipEntries.nextElement();
                if (!zipEntry.isDirectory() && filenameFilter.test(zipEntry.getName())) {
                    try (InputStream stream = new ZipFile(file).getInputStream(zipEntry)) {
                        discovered.add(
                            new Resource<>(
                                "jar:file:" + file.getAbsolutePath() + "!/" + zipEntry.getName(),
                                relative(startPath, file.getAbsolutePath()) + "!/" + zipEntry.getName(),
                                parser.parse(stream, charset)
                            )
                        );
                    } catch (IOException e) {
                        LOGGER.error("{error}", e.getMessage(), e);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("{error}", e.getMessage(), e);
        }
    }


    private String relative(String startPath, String absolutePath) {
        if (absolutePath.endsWith(startPath)) {
            return startPath;
        } else if (absolutePath.contains(startPath)) {
            String partialPath = absolutePath.substring(absolutePath.indexOf(startPath));
            return partialPath.substring(startPath.length() + 1);
        } else {
            String partialPath = absolutePath.substring(startPath.length() - 1);
            return partialPath.substring(startPath.length() + 1);
        }
    }


    protected Enumeration<URL> loadFromClasspath(String classPath, ClassLoader classLoader) {
        try {
            return classLoader.getResources(classPath);
        } catch (IOException e) {
            LOGGER.error("{error}", e.getMessage(), e);
            return Collections.emptyEnumeration();
        }
    }


    private String classLoaderFolder(ClassLoader classLoader) throws IOException {
        try {
            return classLoader.getResource(".").toURI().getPath();
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }



    private byte[] toByteArray(InputStream inputStream) throws IOException {
        try (var outputStream = new ByteArrayOutputStream()) {
            transfer(inputStream, outputStream, new byte[BUFFER_SIZE]);
            return outputStream.toByteArray();
        }
    }


    private String toString(InputStream inputStream, Charset stringCharset) throws IOException {
        return new String(toByteArray(inputStream),stringCharset);
    }


    private void transfer(InputStream input, OutputStream output,byte[] buffer) throws IOException {
         int n;
         while ((n = input.read(buffer)) > 0) {
             output.write(buffer, 0, n);
         }
    }




}
