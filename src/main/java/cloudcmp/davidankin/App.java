package cloudcmp.davidankin;

// /**
//  * Hello world!
//  *
//  */
// public class App 
// {
//   public static void main( String[] args )
//   {
//     System.out.println( "Hello World!" );
//   }
// }


import io.vertx.core.AbstractVerticle;
public class App extends AbstractVerticle {
  public void start() {
    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    }).listen(8080);
  }
}