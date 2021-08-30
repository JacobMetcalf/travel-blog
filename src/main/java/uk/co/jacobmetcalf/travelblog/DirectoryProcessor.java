package uk.co.jacobmetcalf.travelblog;

import com.google.common.collect.ImmutableSortedSet;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.jacobmetcalf.travelblog.htmlrenderer.DiaryTemplate;
import uk.co.jacobmetcalf.travelblog.htmlrenderer.IndexTemplate;
import uk.co.jacobmetcalf.travelblog.htmlrenderer.SimpleElementWriter;
import uk.co.jacobmetcalf.travelblog.model.Diary;
import uk.co.jacobmetcalf.travelblog.model.DiarySummary;
import uk.co.jacobmetcalf.travelblog.model.ImmutableDiarySummary;
import uk.co.jacobmetcalf.travelblog.model.Properties;
import uk.co.jacobmetcalf.travelblog.xmlparser.DiaryParser;

/**
 * Iterates through a directory (recursively if requested) parsing any xml diary files.
 * At the end (if requested) it will generate an index page.
 */
public class DirectoryProcessor {
  private static final String DOCTYPE_HTML = "<!DOCTYPE html>";

  private static final Logger logger = LoggerFactory.getLogger(DirectoryProcessor.class.getName());
  private final ImmutableSortedSet.Builder<DiarySummary> references = ImmutableSortedSet.naturalOrder();

  /**
   * Scans a directory reading XML files and then creating an HTML file.
   * @param directory The directory
   * @param isRecursive Whether to recursively search in the directory.
   * @param createIndex Whether to keep track of all teh diary pages and create an index page.
   * @param properties Additional properties such as social media addresses.
   */
  public void process(final String directory, final boolean isRecursive, final boolean createIndex,
      final Properties properties) {
    int depth = isRecursive ? Integer.MAX_VALUE : 1;
    Path root = Paths.get(directory);
    PathGenerator pathGenerator = new PathGenerator(root, properties);
    try (Stream<Path> paths = Files.find(root, depth, this::isXmlFile)) {
      paths.forEach( path -> processXmlFile(path, pathGenerator, properties));
    } catch (IOException ex) {
      throw new RuntimeException("Could not open directory: " + directory, ex);
    }
    if (createIndex) {
      createIndex(root, pathGenerator, properties);
    }
  }

  private boolean isXmlFile(final Path path, final BasicFileAttributes attributes) {
    return path.getFileName().toString().toLowerCase().endsWith(".xml") &&
        !path.getFileName().toString().equalsIgnoreCase("sitemap.xml");
  }

  /**
   * Parses an XML file of a diary entry then renders as an HTML page using templates.
   */
  private void processXmlFile(final Path inputPath,
      final PathGenerator pathGenerator,
      final Properties properties) {

    // TODO: We should refactor the path generator to be an object passed into others
    Path outputPath = pathGenerator.toDiaryPath(inputPath);
    String canonicalUrl = pathGenerator.toCanonicalUrl(outputPath);
    String canonicalImageUrl = pathGenerator.toCanonicalUrl(pathGenerator.toImagePath(inputPath));

    logger.info("Processing: " + inputPath + ", " + canonicalUrl);

    try (InputStream inputStream = Files.newInputStream(inputPath);
        OutputStream outputStream = Files.newOutputStream(outputPath);
        PrintStream printStream = new PrintStream(outputStream)) {

      DiaryParser diaryParser = new DiaryParser(properties, canonicalUrl, canonicalImageUrl);
      Diary diary = diaryParser.parse(inputStream);

      printStream.println(DOCTYPE_HTML);
      SimpleElementWriter writer = new SimpleElementWriter(printStream);
      new DiaryTemplate(diary, properties, writer).render();

      // keep track of diary entries
      references.add(ImmutableDiarySummary.builder().from(diary).build());

    } catch (IOException | XMLStreamException ex) {
      throw new RuntimeException("Could not process file:" + inputPath, ex);
    }
  }

  /**
   * Creates an index from the saved diary references.
   */
  private void createIndex(final Path root, final PathGenerator pathGenerator,
      final Properties properties) {

    Path outputPath = pathGenerator.toIndexPath(root);
    logger.info("Creating index file: " + outputPath);

    try (OutputStream outputStream = Files.newOutputStream(outputPath);
        PrintStream printStream = new PrintStream(outputStream)) {
      SimpleElementWriter writer = new SimpleElementWriter(printStream);
      new IndexTemplate(references.build(), properties, writer).render();

    } catch (IOException ex) {
      throw new RuntimeException("Could not create file:" + outputPath, ex);
    }
  }
}
