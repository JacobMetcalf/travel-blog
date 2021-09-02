package uk.co.jacobmetcalf.travelblog.executor;

import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PathsTest {
  private final Properties testProperties = Properties.of(Properties.Key.CANONICAL_URL, "https://my.site.com");
  private final Paths unit = new Paths(Path.of("Website"),
      Path.of("Website", "china", "china-gansu.xml"), testProperties);

  @Test
  public void to_diary_path() {
    Path actual = unit.getDiaryOutputPath();
    Path expected = Path.of("Website", "china", "china-gansu-new.html");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void to_index_path() {
    Path actual = unit.getIndexOutputPath();
    Path expected = Path.of("Website", "index-new.html");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void to_canonical_url() {
    String actual = unit.getCanonicalUrl();
    Assertions.assertEquals(actual, "https://my.site.com/china/china-gansu-new.html");
  }

  @Test
  public void to_relative_image_url() {
    String actual = unit.getImageUrlRelativeToRoot("thumb.jpg");
    Assertions.assertEquals(actual, "china/images/thumb.jpg");
  }

  @Test
  public void throws_on_canonical_url_with_no_slash() {
    Properties incorrectProperties = Properties.of(Properties.Key.CANONICAL_URL, "https://my.site.com/");
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> new Paths(Path.of("Website"), Path.of("Website"), incorrectProperties));
  }
}
