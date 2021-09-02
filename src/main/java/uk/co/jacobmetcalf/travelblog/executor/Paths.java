package uk.co.jacobmetcalf.travelblog.executor;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import java.nio.file.Path;
import org.apache.commons.io.FilenameUtils;

/**
 * Generates paths where to output files to as well as relative locations for hrefs etc.
 * The canonical URL is used for links and is used by Google to index pages.
 */
public class Paths {
  private final String canonicalUrl;
  private final Path rootPath;
  private final Path xmlPath;

  /**
   * Creates a collections of paths.
   * @param rootPath The root directory where the website is being generated from.
   * @param xmlPath The path where the xml file was found.
   * @param properties The properties collection.
   */
  public Paths(final Path rootPath, final Path xmlPath, final Properties properties) {
    this.canonicalUrl = properties.get(Properties.Key.CANONICAL_URL)
        .orElseThrow(() -> new IllegalArgumentException("Canonical URL must be provided."));
    Preconditions.checkArgument(!canonicalUrl.endsWith("/"),
        "Canonical URL does not need a trailing slash.");
    this.rootPath = rootPath;
    this.xmlPath = xmlPath;
  }

  public Path getDiaryOutputPath() {
    Path directory = xmlPath.getParent();
    String newFileName = FilenameUtils.removeExtension(xmlPath.getFileName().toString().toLowerCase())
        + "-new.html";
    return directory.resolve(newFileName);
  }

  public Path getIndexOutputPath() {
    return rootPath.resolve("index-new.html");
  }

  public String getCanonicalUrl() {
    Path relativeDiaryPath = rootPath.relativize(getDiaryOutputPath());
    return canonicalUrl + "/" + Joiner.on('/').join(relativeDiaryPath);
  }

  public String getDiaryUrlRelativeToRoot() {
    Path relativeDiaryPath = rootPath.relativize(getDiaryOutputPath());
    return Joiner.on('/').join(relativeDiaryPath);
  }

  public String getImageUrlRelativeToRoot(final String imageName) {
    Path imagePath = xmlPath.getParent().resolve("images").resolve(imageName);
    Path relativeImagePath = rootPath.relativize(imagePath);
    return Joiner.on('/').join(relativeImagePath);
  }
}
