package cloudcmp.davidankin.project1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class App extends AbstractVerticle {
  public void start() {
    Router router = Router.router(vertx);
    router.route().handler(LoggerHandler.create());

    router.route("/home").handler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    });

    String jarResourceName = "webroot";
    router.route("/*").handler(StaticHandler.create(jarResourceName));
    router.route().handler(req -> {
      req.fail(404);
    });

    HttpServer s = vertx.createHttpServer();
    s.requestHandler(router);
    s.listen(8080);
  }
}