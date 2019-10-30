package cloudcmp.davidankin.project1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.BodyHandler;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Set;
import java.util.Iterator;
import java.io.File;

public class App extends AbstractVerticle {
  private final Logger LOGGER = LoggerFactory.getLogger(App.class);
  private SSH ssh;
  public void start() {
    try {
      ssh = new SSH();
      // ssh.connect("unixs.cssd.pitt.edu", "daa85");
      // LOGGER.error("Connected SSH");
    } catch (Exception e) {
      LOGGER.error("SSH Failed to connect");
      LOGGER.error(e.toString());
    }

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.route().handler(LoggerHandler.create());
    router.route("/home").blockingHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    });
    router.post("/upload").blockingHandler(ctx -> {
      LOGGER.error("have post route");
      int counter = 0;
      try {
        for (FileUpload f: ctx.fileUploads()) {
          counter++;
          LOGGER.error("contentType:      " + f.contentType());
          LOGGER.error("fileName:         " + f.fileName());
          LOGGER.error("name:             " + f.name());
          LOGGER.error("size:             " + f.size());
          LOGGER.error("uploadedFileName: " + f.uploadedFileName());
          File file = new File(f.uploadedFileName());
          file.renameTo(new File(f.fileName()));
          file = null;
        }
        LOGGER.error("have counter " + counter + " and size " + ctx.fileUploads().size());

        ctx.response()
          .putHeader("content-type", "text/plain")
          .end("hey " + counter);
      } catch (Exception e) {
        LOGGER.error("Caught exception");
        e.printStackTrace();
        ctx.response()
          .putHeader("content-type", "text/plain")
          .end("hey " + counter);
      }
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