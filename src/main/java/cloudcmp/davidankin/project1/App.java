package cloudcmp.davidankin.project1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.BodyHandler;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Set;
import java.util.Iterator;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class App extends AbstractVerticle {
  private final Logger LOGGER = LoggerFactory.getLogger(App.class);
  private SSH ssh;
  public void start() {
    LOGGER.info("HELLO WORLD");
    ssh = new SSH();
    ssh.connect("unixs.cssd.pitt.edu", "daa85");
    // ssh.connect("ric-edge-01.sci.pitt.edu", "daa85");

    Router uploadRouter = Router.router(vertx);
    uploadRouter.route().handler(BodyHandler.create());
    uploadRouter.post("/").blockingHandler(ctx -> {
      int counter = 0;
      try {
        for (FileUpload f: ctx.fileUploads()) {
          counter++;
          LOGGER.info("contentType:      " + f.contentType());
          LOGGER.info("fileName:         " + f.fileName());
          LOGGER.info("name:             " + f.name());
          LOGGER.info("size:             " + f.size());
          LOGGER.info("uploadedFileName: " + f.uploadedFileName());

          Path from = Paths.get(f.uploadedFileName());
          Path to = Paths.get("file-uploads", f.fileName());
          LOGGER.info("moving from " + from + " to " + to);
          Path temp = Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
          LOGGER.info("Uploading: " + to.toString());

          boolean success = ssh.upload(to.toString(), f.fileName());
          if (!success)
            LOGGER.error("Failed to Upload files in uploadRouter");
        }

        LOGGER.error("Uploaded " + counter + " files.");

        String cmd = "./collectDataAndConstruct.sh";
        String output = ssh.run(cmd);
        if (output == null) {
          throw new Exception("Failed to run " + cmd + " in uploadRouter /");
        }

        LOGGER.info("Ran " + cmd + " on cluster.");

        ctx.response()
          .putHeader("content-type", "text/plain")
          .end("counter:" + counter + ":" + output);
      } catch (Exception e) {
        LOGGER.error("Caught exception");
        e.printStackTrace();
        ctx.response()
          .putHeader("content-type", "text/plain")
          .end("error");
      }
    });

    Router mainRouter = Router.router(vertx);
    mainRouter.route().handler(LoggerHandler.create());
    mainRouter.mountSubRouter("/upload", uploadRouter);

    Router router = mainRouter;
    router.route("/home").handler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    });
    router.post("/json").handler(BodyHandler.create()).handler(req -> {
      LOGGER.info("uploading");
      try {
        ssh.upload("/home/toor/examples.desktop", null);
      } catch (Exception e) {
        LOGGER.error("ok bad");
        LOGGER.error(e.toString());
      }
      JsonObject body = req.getBodyAsJson();
      body.put("ok", "ok");
      req.response()
        .putHeader("content-type", "application/json")
        .end(body.toString());
    });

    String jarResourceName = "webroot";
    router.route("/*").handler(StaticHandler.create(jarResourceName));
    router.route().handler(req -> {
      req.fail(404);
    });

    HttpServer s = vertx.createHttpServer();
    s.requestHandler(mainRouter);
    s.listen(8080);
  }
}