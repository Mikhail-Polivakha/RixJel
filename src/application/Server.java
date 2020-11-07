package application;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

/**
 * @author Mikhail Polivakha
 * @created 11/6/2020
 */

public class Server implements PropertyHolder {
    private AsynchronousServerSocketChannel serverSocketChannel;
    private static int BUFFER_SIZE;
    private static final String HTTP_SERVER_RESPONSE_HEADER =
            "HTTP/1.1 200 OK\n" +
            "application.Server: tomcat-2.0\n" +
            "Content-Type: text/html\n" +
            "Content-Length: %s\n" +
            "Connection: close\n\n";

    private final RequestHandler requestHandler;

    public Server(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void bootstrap() {
        try {
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8088));

            while (true) {
                handleClientRequest();
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void handleClientRequest() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        final Future<AsynchronousSocketChannel> accept = serverSocketChannel.accept();
        System.out.println("new client connection");

        final AsynchronousSocketChannel clientChannel = accept.get();

        while (clientChannel != null && clientChannel.isOpen()) {
            final ByteBuffer temporaryBufferStoresClientRequest = ByteBuffer.allocate(BUFFER_SIZE);
            StringBuilder stringBuilderRepresentationOfClientRequest = new StringBuilder();
            boolean continueRead = true;

            while (continueRead) {

                int numberOfReadBytes = clientChannel.read(temporaryBufferStoresClientRequest).get();
                continueRead = temporaryBufferStoresClientRequest.position() == BUFFER_SIZE;

                final byte[] array = continueRead
                    ? temporaryBufferStoresClientRequest.array()
                    : Arrays.copyOfRange(temporaryBufferStoresClientRequest.array(),
                        0, temporaryBufferStoresClientRequest.position());

                stringBuilderRepresentationOfClientRequest.append(new String(array));
                temporaryBufferStoresClientRequest.clear();
            }

            final HttpRequest httpRequest = new HttpRequest(
                    stringBuilderRepresentationOfClientRequest.toString()
            );

            final HttpResponse httpResponse = new HttpResponse();


            final String body = requestHandler.handle(httpRequest, httpResponse);

            final String responseText = String.format(HTTP_SERVER_RESPONSE_HEADER, body.length()) + body;
            ByteBuffer responseBytes = ByteBuffer.wrap(responseText.getBytes());

            clientChannel.write(responseBytes);
            clientChannel.close();
        }
    }
}
