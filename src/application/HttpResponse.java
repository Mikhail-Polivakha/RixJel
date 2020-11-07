package application;

import application.ServerResponseStatusCode;

import java.util.Map;

/**
 * @author Mikhail Polivakha
 * @created 11/6/2020
 */

public class HttpResponse implements PropertyHolder {
    private final String ROW_DELIMITER = "\r\n";

    private Map<String, String> responseHeaders;
    private String body;
    private ServerResponseStatusCode statusCode;
    private StringBuilder responseMessage;

    public HttpResponse() {
        responseHeaders.put("application.Server", "tomcat-2.0");
        responseHeaders.put("Connection", "Close");
    }

    public void addHeader(String key, String value) {
        responseHeaders.put(key, value);
    }

    public void addHeaders(Map<String, String> headers) {
        responseHeaders.putAll(headers);
    }

    private void buildResponseMessage() {
        responseMessage.append("HTTP/1.1")
                .append(statusCode.getStatusCode() + " ")
                .append(statusCode.name())
                .append(ROW_DELIMITER);

        responseHeaders.forEach((key, value) ->
                responseMessage.append(key)
                                .append(": ")
                                .append(value)
                                .append(ROW_DELIMITER)
        );

        responseMessage.append(ROW_DELIMITER)
                       .append(body);
    }

    public byte[] getServerResponseMessageAsByteArray() {
        this.buildResponseMessage();
        return responseMessage.toString().getBytes();
    }

    public void setResponseMessageBody(String body) {
        responseHeaders.put("Content-Length", String.valueOf(body.length()));
        this.body = body;
    }
}
