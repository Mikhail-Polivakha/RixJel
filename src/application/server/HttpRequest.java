package application.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Polivakha
 * @created 11/6/2020
 */
public class HttpRequest {

    private final String HEADER_BODY_DELIMITER = "\r\n\r\n";
    private final String ROW_DELIMITER = "\r\n";
    private final String HEADER_KEY_VALUE_SEPARATOR = ":";

    private final String message;
    private final HttpMethod httpMethod;
    private final String url;
    private final Map<String, String> headers;
    private final String requestBody;

    public HttpRequest(String message) {
        this.message = message;

        final String[] requestParts = message.split(HEADER_BODY_DELIMITER);
        final String[] headersArray = requestParts[0].split(ROW_DELIMITER);
        final String[] partsOfFirstHeader = headersArray[0].split(" ");

        this.httpMethod = HttpMethod.valueOf(partsOfFirstHeader[0]);
        this.url = partsOfFirstHeader[1];
        this.headers = Collections.unmodifiableMap(
            buildHeadersMapFromHeadersArray(Arrays.copyOfRange(headersArray, 1, headersArray.length))
        );

        this.requestBody = getRequestBody(requestParts);
    }

    private String getRequestBody(String[] requestParts) {
        final String contentLength = headers.get("Content-Length");
        if (contentLength != null) {
            final int intContentLength = Integer.parseInt(contentLength);
            return requestParts[1].trim().substring(0, intContentLength);
        } else {
            return null;
            // todo
        }
    }

    private Map<String, String> buildHeadersMapFromHeadersArray(String[] headers) {
        Map<String, String> headerMap = new HashMap<>();
        Arrays.stream(headers).forEach(header -> {
            final String[] keyValueHeaderPair =
                    header.split(HEADER_KEY_VALUE_SEPARATOR, 2);
            headerMap.put(keyValueHeaderPair[0].trim(), keyValueHeaderPair[1].trim());
        });
        return headerMap;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getRequestBody() {
        return requestBody;
    }
}
