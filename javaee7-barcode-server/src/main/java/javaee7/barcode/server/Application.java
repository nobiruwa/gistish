package javaee7.barcode.server;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

// http://localhost:8080/javaee7-barcode-server/resources
// 配下にJAX-RSのリソースを公開する
@ApplicationPath("resources")
public class Application extends ResourceConfig {
    public Application() {
        // リソースが実装されているパッケージを
        // ';'区切りで列挙する
        packages("javaee7.barcode.server.resources");
    }
}
