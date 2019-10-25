package cloudcmp.davidankin.project1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;

@ExtendWith(VertxExtension.class)
public class AppTest {
  @Test
  void addition() {
    assertEquals(2, 2);
  }
}
