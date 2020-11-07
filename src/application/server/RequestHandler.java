package application.server;

/**
 * @author Mikhail Polivakha
 * @created 11/6/2020
 */
public interface RequestHandler {
    public String handle(HttpRequest request, HttpResponse response);
}
