package uk.co.jacobmetcalf.travelblog;

import java.nio.file.Path;
import org.apache.commons.io.FilenameUtils;

/**
 * Names a file based on the original xml file name and directory.
 */
public class FileNamer {
  public Path toHtmlFile(Path inputPath) {
    Path directory = inputPath.getParent();
    String newFileName = FilenameUtils.removeExtension(inputPath.getFileName().toString().toLowerCase())
        + "-new.html";
    return directory.resolve(newFileName);
  }
}
