package org.example;

import com.sun.net.httpserver.HttpServer;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RequestOptions;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        HttpServer server = null;
        try {
            String hostname = System.getenv("HOSTNAME");
            server = HttpServer.create(new InetSocketAddress(hostname, 8001), 0);
        } catch (java.io.IOException e) {
            System.out.println("Error occurred");
            System.exit(1);
        }
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        server.createContext("/healthcheck", new  MyHttpHandler());
        server.setExecutor(threadPoolExecutor);

        System.out.println(" Server started on port 8001");
        server.start();
    }
}
