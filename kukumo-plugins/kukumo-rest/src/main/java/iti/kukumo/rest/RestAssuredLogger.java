/**
 * @author Luis Iñesta Gelabert - linesta@iti.es | luiinge@gmail.com
 */
package iti.kukumo.rest;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.slf4j.Logger;


public class RestAssuredLogger {

    private final Logger logger;
    private PrintStream printStream;


    public RestAssuredLogger(Logger logger) {
        this.logger = logger;
    }


    public PrintStream getPrintStream() {
        if (printStream == null) {
            OutputStream output = new OutputStream() {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();


                @Override
                public void write(int b) throws IOException {
                    baos.write(b);
                }


                @Override
                public void flush() {
                    logger.debug(this.baos.toString());
                    baos = new ByteArrayOutputStream();
                }
            };
            printStream = new PrintStream(output, true); // true: autoflush must be set!
        }

        return printStream;
    }

}
