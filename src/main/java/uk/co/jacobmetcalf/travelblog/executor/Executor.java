package uk.co.jacobmetcalf.travelblog.executor;

import com.google.common.collect.ImmutableSortedSet;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.jacobmetcalf.travelblog.htmlrenderer.DiaryTemplate;
import uk.co.jacobmetcalf.travelblog.htmlrenderer.IndexTemplate;
import uk.co.jacobmetcalf.travelblog.htmlrenderer.SimpleElementWriter;
import uk.co.jacobmetcalf.travelblog.model.Diary;
import uk.co.jacobmetcalf.travelblog.model.IndexEntry;
import uk.co.jacobmetcalf.travelblog.model.ImmutableIndexEntry;
import uk.co.jacobmetcalf.travelblog.xmlparser.DiaryParser;

/**
 * Iterates through a directory (recursively if requested) parsing any xml diary files.
 * At the end (if requested) it will generate an index page.
 */
public class Executor {
  private static final String DOCTYPE_HTML = "<!DOCTYPE html>";

  private static final Logger logger = LoggerFactory.getLogger(Executor.class.getName());
  private final ImmutableSortedSet.Builder<IndexEntry> references = ImmutableSortedSet.naturalOrder();

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
    Path root = java.nio.file.Paths.get(directory);
    try (Stream<Path> paths = Files.find(root, depth, this::isXmlFile)) {
      paths.forEach(path -> processXmlFile(root, path, properties));
    } catch (IOException ex) {
      throw new RuntimeException("Could not open directory: " + directory, ex);
    }
    if (createIndex) {
      createIndex(new Paths(root, root, properties), properties);
    }
  }

  private boolean isXmlFile(final Path path, final BasicFileAttributes attributes) {
    return path.getFileName().toString().toLowerCase().endsWith(".xml") &&
        !path.getFileName().toString().equalsIgnoreCase("sitemap.xml");
  }

  /**
   * Parses an XML file of a diary entry then renders as an HTML page using templates.
   */
  private void processXmlFile(final Path rootPath,
      final Path inputPath,
      final Properties properties) {

    Paths paths = new Paths(rootPath, inputPath, properties);
    Path outputPath = paths.getDiaryOutputPath();
    logger.info("Processing: " + inputPath + ", " + paths.getCanonicalUrl());

    try (InputStream inputStream = Files.newInputStream(inputPath);
        OutputStream outputStream = Files.newOutputStream(outputPath);
        PrintStream printStream = new PrintStream(outputStream)) {

      Diary diary = parse(properties, paths, inputStream);

      // Now
      printStream.println(DOCTYPE_HTML);
      SimpleElementWriter writer = new SimpleElementWriter(printStream);
      new DiaryTemplate(diary, properties, writer).render();

      // keep track of diary entries
      references.add(ImmutableIndexEntry.builder().from(diary)
          .date(diary.getEntriesAndRoutes().getStartDate())
          .title(diary.getTitle())
          .summary(diary.getSummary())
          .relativeUrl(paths.getDiaryUrlRelativeToRoot())
          .relativeThumbUrl(diary.getThumb().map(paths::getImageUrlRelativeToRoot))
          .build());

    } catch (IOException | XMLStreamException ex) {
      throw new RuntimeException("Could not process file:" + inputPath, ex);
    }
  }

  /**
   * First partially parse the diary. Note that as an exercise
   * this does not pull the entries for each day
   */
  private Diary parse(Properties properties, Paths paths, InputStream inputStream)
      throws XMLStreamException {
    DiaryParser diaryParser = new DiaryParser(properties, paths);
    return diaryParser.parse(inputStream);
  }

  /**
   * Creates an index from the saved diary references.
   */
  private void createIndex(final Paths paths,
      final Properties properties) {

    Path outputPath = paths.getIndexOutputPath();
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
