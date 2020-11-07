package application;

/**
 * @author Mikhail Polivakha
 * @created 11/6/2020
 */
public enum ServerResponseStatusCode {
    OK(200),
    CREATED(201),
    FORBIDDEN(403),
    METHOD_NOT_ALLOWED(405),
    NOT_FOUND(404);

    private final int statusCode;

    ServerResponseStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
