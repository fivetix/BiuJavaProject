package server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    public static RequestInfo parseRequest(BufferedReader reader) throws IOException {
        String httpCommand = null;
        String uri = null;
        String[] uriSegments = null;
        Map<String, String> parametersFromURI = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        byte[] content = null;

        String requestLine = reader.readLine();
        if (requestLine != null && !requestLine.isEmpty()) {
            String[] requestParts = requestLine.split(" ");
            httpCommand = requestParts[0];
            uri = requestParts[1];

            String[] uriAndParams = uri.split("\\?");
            uriSegments = uriAndParams[0].substring(1).split("/");
            if (uriAndParams.length > 1) {
                String[] paramPairs = uriAndParams[1].split("&");
                for (String pair : paramPairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length == 2) {
                        parametersFromURI.put(keyValue[0], keyValue[1]);
                    }
                }
            }
        }

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(": ");
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
        }

        StringBuilder contentBuilder = new StringBuilder();
        while (reader.ready() && (line = reader.readLine()) != null && !line.isEmpty()) {
            contentBuilder.append(line).append("\n");
        }

        if (headers.containsKey("Content-Length") && Integer.parseInt(headers.get("Content-Length")) > 0) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int ch;
            while ((ch = reader.read()) != -1) {
                byteArrayOutputStream.write(ch);
            }
            content = byteArrayOutputStream.toByteArray();
        }

        Map<String, String> finalParameters = new HashMap<>(parametersFromURI);
        headers.forEach((key, value) -> {
            if (!key.equalsIgnoreCase("Host") && !key.equalsIgnoreCase("Content-Length")) {
                finalParameters.put(key, value);
            }
        });

        System.out.println("Request parsed:");
        System.out.println("HTTP Command: " + httpCommand);
        System.out.println("URI: " + uri);
        System.out.println("Headers: " + headers);
        System.out.println("Parameters: " + finalParameters);
        System.out.println("Content: " + new String(content != null ? content : new byte[0]));

        return new RequestInfo(httpCommand, uri, uriSegments, finalParameters, headers, content);
    }

    public static class RequestInfo {
        private final String httpCommand;
        private final String uri;
        private final String[] uriSegments;
        private final Map<String, String> parameters;
        private final Map<String, String> headers;
        private final byte[] content;

        public RequestInfo(String httpCommand, String uri, String[] uriSegments, Map<String, String> parameters, Map<String, String> headers, byte[] content) {
            this.httpCommand = httpCommand;
            this.uri = uri;
            this.uriSegments = uriSegments;
            this.parameters = parameters;
            this.headers = headers;
            this.content = content;
        }

        public String getHttpCommand() {
            return httpCommand;
        }

        public String getUri() {
            return uri;
        }

        public String[] getUriSegments() {
            return uriSegments;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public byte[] getContent() {
            return content;
        }
    }
}
