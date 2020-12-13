package uk.co.jacobmetcalf.travelblog;

import com.beust.jcommander.Parameter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.io.FilenameUtils;
import uk.co.jacobmetcalf.travelblog.htmlrenderer.DiaryTemplate;
import uk.co.jacobmetcalf.travelblog.htmlrenderer.SimpleElementWriter;
import uk.co.jacobmetcalf.travelblog.model.Diary;
import uk.co.jacobmetcalf.travelblog.xmlparser.DiaryParser;

public class Executor {
  private List<String> directories = new ArrayList<>();
  private String googleKey;
  private String amazonKey;
  private String linkedInId;
  private boolean recursive = false;
  private boolean help = false;
  private final DiaryParser diaryParser = new DiaryParser();

  private static final Logger logger = LoggerFactory.getLogger(Executor.class.getName());

  @Parameter(description = "List of directories", required = true)
  public void setDirectories(List<String> directories) {
    this.directories = directories;
  }

  @Parameter(names = {"--googlekey", "-g"}, description = "Google api key", required = true)
  public void setGoogleKey(String googleKey) {
    this.googleKey = googleKey;
  }

  @Parameter(names = {"--amazonkey", "-a"}, description = "Amazon associates key")
  public void setAmazonKey(String amazonKey) {
    this.amazonKey = amazonKey;
  }

  @Parameter(names = {"--linkedin", "-l"}, description = "Linked in id")
  public void setLinkedInId(String linkedInId) {
    this.linkedInId = linkedInId;
  }

  @Parameter(names = {"--recursive", "-r"}, description = "Whether to recursively search sub-directories of the given directory")
  public void setRecursive(boolean recursive) {
    this.recursive = recursive;
  }

  @Parameter(names = { "--help", "-h" }, help = true)
  public void setHelp(boolean help) {
    this.help = help;
  }

  public boolean isHelp() {
      return help;
    }

  public void execute() {
    directories.forEach(this::processPath);
  }

  private void processPath(final String directory) {
    try (Stream<Path> paths = Files.walk(Paths.get(directory),
        recursive ? Integer.MAX_VALUE : 1)) {
      paths.filter(this::isXmlFile)
          .forEach(this::processXmlFile);

    } catch (IOException ex) {
      throw new RuntimeException("Could not open directory: " + directory, ex);
    }
  }

  private boolean isXmlFile(final Path path) {
    return path.getFileName().toString().toLowerCase().endsWith(".xml");
  }

  private void processXmlFile(final Path inputPath){
    logger.info("Processing: " + inputPath);
    Path outputPath = Paths.get(FilenameUtils.removeExtension(inputPath.toString()) + "-new.html");

    try (InputStream inputStream = Files.newInputStream(inputPath);
        OutputStream outputStream = Files.newOutputStream(outputPath);
        PrintStream printStream = new PrintStream(outputStream)) {

      Diary diary = diaryParser.parse(inputPath.getFileName().toString(), inputStream);
      SimpleElementWriter writer = new SimpleElementWriter(printStream);
      new DiaryTemplate(diary, googleKey, amazonKey, linkedInId, writer).render();

    } catch (IOException | XMLStreamException ex) {
      throw new RuntimeException("Could not process file:" + inputPath, ex);
    }
  }
}
