package LLD.DesignPattern.creationalPattern.builder.thirdResource.RESTAPIclient;


 interface Request {
    void execute(); 
    String getHeaders();
    String getBody();
    String getAuthToken();
}


 interface RequestBuilder {
    RequestBuilder setHeader(String key, String value);
    RequestBuilder setBody(String body);
    RequestBuilder setAuth(String token);
    RequestBuilder setTimeout(int milliseconds);
    Request build();
}

 class DefaultRequest implements Request {
    private final String headers;
    private final String body;
    private final String authToken;
    private final int timeout;

    public DefaultRequest(String headers, String body, String authToken, int timeout) {
        this.headers = headers;
        this.body = body;
        this.authToken = authToken;
        this.timeout = timeout;
    }

    @Override
    public void execute() {
        System.out.println("Executing request with:");
        System.out.println("Headers: " + headers);
        System.out.println("Body: " + body);
        System.out.println("Auth: " + (authToken != null ? "***" : "None"));
        System.out.println("Timeout: " + timeout + "ms");
    }

    @Override
    public String getHeaders() { return headers; }

    @Override
    public String getBody() { return body; }

    @Override
    public String getAuthToken() { return authToken; }
}



class DefaultRequestBuilder implements RequestBuilder {
    private String headers = "";
    private String body = "";
    private String authToken = null;
    private int timeout = 3000; // Default timeout

    @Override
    public RequestBuilder setHeader(String key, String value) {
        if (!headers.isEmpty()) headers += "\n";
        headers += key + ": " + value;
        return this;
    }

    @Override
    public RequestBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    @Override
    public RequestBuilder setAuth(String token) {
        this.authToken = "Bearer " + token;
        return this;
    }

    @Override
    public RequestBuilder setTimeout(int milliseconds) {
        this.timeout = milliseconds;
        return this;
    }

    @Override
    public Request build() {
        return new DefaultRequest(headers, body, authToken, timeout);
    }
}


public class Main {
    public static void main(String[] args) {
        // Fluent API usage
        Request request = new DefaultRequestBuilder()
                .setHeader("Content-Type", "application/json")
                .setHeader("Accept", "application/json")
                .setBody("{ \"name\": \"John Doe\" }")
                .setAuth("abc123")
                .setTimeout(5000)
                .build();

        request.execute();
    }
}