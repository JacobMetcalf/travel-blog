package uk.co.jacobmetcalf.travelblog;

import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.Properties;

public class PathGeneratorTest {
  private final Properties testProperties = Properties.of(Properties.Key.CANONICAL_URL, "https://my.site.com");
  private final PathGenerator unit = new PathGenerator(Path.of("Website"), testProperties);

  @Test
  public void converts_to_xml() {
    Path input = Path.of("Website", "china", "china-gansu.xml");
    Path actual = unit.toDiaryPath(input);
    Path expected = Path.of("Website", "china", "china-gansu-new.html");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void to_canonical_url() {
    Path input = Path.of("Website", "china", "china-gansu.html");
    String actual = unit.toCanonicalUrl(input);
    Assertions.assertEquals(actual, "https://my.site.com/china/china-gansu.html");
  }

  @Test
  public void canonical_url_no_slash() {
    Properties incorrectProperties = Properties.of(Properties.Key.CANONICAL_URL, "https://my.site.com/");
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> new PathGenerator(Path.of("Website"), incorrectProperties));
  }
}
