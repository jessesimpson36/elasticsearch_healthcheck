package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.client.*;

import java.io.IOException;
import java.io.OutputStream;

public class MyHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response=null;
        if("GET".equals(httpExchange.getRequestMethod())) {
            response = handleGetRequest(httpExchange);
        }
        handleResponse(httpExchange,response);
    }
    private String handleGetRequest(HttpExchange httpExchange) {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost("localhost", 9200, "http")
        );
        final RestHighLevelClient esClient = new RestHighLevelClientBuilder(builder.build())
                .setApiCompatibilityMode(true)
                .build();
        int numberOfClusterNodes = 0;

        try {
            numberOfClusterNodes = esClient.cluster()
                    .health(new ClusterHealthRequest(), RequestOptions.DEFAULT)
                    .getNumberOfNodes();
        } catch (java.io.IOException e) {
            System.out.println("Error occurred");
            return "";
        }

        System.out.println("Number of cluster nodes: " + String.valueOf(numberOfClusterNodes));

        try {
            esClient.close();
        } catch (java.io.IOException e) {
            System.out.println("Error occurred");
            return "";
        }

        return String.valueOf(numberOfClusterNodes);
    }
    private void handleResponse(HttpExchange httpExchange, String response)  throws  IOException {
        int statusCode = 200;
        String message = response;
        if (response.isEmpty()) {
            statusCode = 400;
            message = "Error";
        }
        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("{").
        append("\"cluster_number\": ")
                .append(message)
                .append("}");
        // encode HTML content
        String htmlResponse = htmlBuilder.toString();
        // this line is a must
        httpExchange.sendResponseHeaders(statusCode, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}

