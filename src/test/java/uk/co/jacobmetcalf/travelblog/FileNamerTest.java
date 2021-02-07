package uk.co.jacobmetcalf.travelblog;

import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileNamerTest {
  @Test
  public void converts_to_xml() {
    Path input = Path.of("directory", "sub-directory", "China", "china-gansu.xml");
    Path actual = new FileNamer().toHtmlFile(input);
    Path expected = Path.of("directory", "sub-directory", "China", "china-gansu-new.html");
    Assertions.assertEquals(actual, expected);
  }
}
