package uk.co.jacobmetcalf.travelblog;

import com.google.common.base.Preconditions;
import java.nio.file.Path;
import org.apache.commons.io.FilenameUtils;
import uk.co.jacobmetcalf.travelblog.model.Properties;

/**
 * Given where the file is being output generates the canonical URL.
 * The canonical URL is used for links and is used by Google to index pages.
 */
public class PathGenerator {
  private final String canonicalUrl;
  private final Path rootPath;

  public PathGenerator(final Path rootPath, final Properties properties) {
    this.canonicalUrl = properties.get(Properties.Key.CANONICAL_URL).orElseThrow();
    this.rootPath = rootPath;
    Preconditions.checkArgument(!canonicalUrl.endsWith("/"),
        "Canonical URL does not need a trailing slash.");
  }

  public Path toDiaryPath(final Path xmlPath) {
    Path directory = xmlPath.getParent();
    String newFileName = FilenameUtils.removeExtension(xmlPath.getFileName().toString().toLowerCase())
        + "-new.html";
    return directory.resolve(newFileName);
  }

  public Path toImagePath(final Path xmlPath) {
    Path directory = xmlPath.getParent();
    return directory.resolve("images");
  }

  public Path toIndexPath(final Path rootPath) {
    return rootPath.resolve("index-new.html");
  }

  public String toCanonicalUrl(final Path filePath) {
    StringBuilder result = new StringBuilder();
    result.append(canonicalUrl);
    for (Path part : rootPath.relativize(filePath)) {
      result.append('/');
      result.append(part.toString());
    }
    return result.toString();
  }

  public String toImagesUrl(final Path filePath) {
    StringBuilder result = new StringBuilder().append(canonicalUrl);
    Path imagePath = filePath.getParent().resolve("images");
    for (Path part : rootPath.relativize(imagePath)) {
      result.append('/');
      result.append(part.toString());
    }
    return result.toString();
  }
}
