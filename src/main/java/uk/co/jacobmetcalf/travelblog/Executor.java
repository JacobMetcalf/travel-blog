package uk.co.jacobmetcalf.travelblog;

import com.beust.jcommander.Parameter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.jacobmetcalf.travelblog.htmlrenderer.DiaryTemplate;
import uk.co.jacobmetcalf.travelblog.htmlrenderer.ImmutableProperties;
import uk.co.jacobmetcalf.travelblog.model.Properties;
import uk.co.jacobmetcalf.travelblog.model.Properties.Key;
import uk.co.jacobmetcalf.travelblog.htmlrenderer.SimpleElementWriter;
import uk.co.jacobmetcalf.travelblog.model.Diary;
import uk.co.jacobmetcalf.travelblog.xmlparser.DiaryParser;

public class Executor {

  public static final String DOCTYPE_HTML = "<!DOCTYPE html>";
  private static final Logger logger = LoggerFactory.getLogger(Executor.class.getName());

  private final FileNamer fileNamer = new FileNamer();

  private List<String> directories = new ArrayList<>();
  private ImmutableProperties.Builder properties = ImmutableProperties.builder();
  private boolean recursive = false;
  private boolean help = false;


  @Parameter(description = "List of directories", required = true)
  public void setDirectories(List<String> directories) {
    this.directories = directories;
  }

  @Parameter(names = {"--url", "-u"},
      description = "Canonical (i.e. the one you want Google to index) url", required = true)
  public void setCanonicalUrl(String canonicalUrl) {
    properties.putValue(Key.CANONICAL_URL, canonicalUrl);
  }

  @Parameter(names = {"--googlekey", "-g"}, description = "Google api key for maps (mandatory)", required = true)
  public void setGoogleKey(String googleKey) {
    properties.putValue(Key.GOOGLE_API_KEY, googleKey);
  }

  @Parameter(names = {"--amazonkey", "-a"}, description = "Amazon associates key for selling books")
  public void setAmazonKey(String amazonKey) {
    properties.putValue(Key.AMAZON_ASSOCIATES_ID, amazonKey);
  }

  @Parameter(names = {"--linkedin", "-l"}, description = "Linked in id")
  public void setLinkedInId(String linkedIn) {
    properties.putValue(Key.LINKED_IN, linkedIn);
  }

  @Parameter(names = {"--facebook", "-f"}, description = "Facebook id")
  public void setFacebook(String facebook) {
    properties.putValue(Key.FACEBOOK, facebook);
  }

  @Parameter(names = {"--twitter", "-t"}, description = "Twitter id")
  public void setTwitter(String twitter) {
    properties.putValue(Key.TWITTER, twitter);
  }

  @Parameter(names = {"--github", "-b"}, description = "GitHub account")
  public void setGitHub(String gitHub) {
    properties.putValue(Key.GITHUB, gitHub);
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
    int depth = recursive ? Integer.MAX_VALUE : 1;
    try (Stream<Path> paths = Files.find(Paths.get(directory), depth, this::isXmlFile)) {
          paths.forEach(this::processXmlFile);

    } catch (IOException ex) {
      throw new RuntimeException("Could not open directory: " + directory, ex);
    }
  }

  private boolean isXmlFile(final Path path, final BasicFileAttributes attributes) {
    return path.getFileName().toString().toLowerCase().endsWith(".xml") &&
        !path.getFileName().toString().equalsIgnoreCase("sitemap.xml");
  }

  private void processXmlFile(final Path inputPath){
    Path outputPath = fileNamer.toHtmlFile(inputPath);
    logger.info("Processing: " + inputPath);

    try (InputStream inputStream = Files.newInputStream(inputPath);
        OutputStream outputStream = Files.newOutputStream(outputPath);
        PrintStream printStream = new PrintStream(outputStream)) {

      Properties properties = this.properties.build();
      DiaryParser diaryParser = new DiaryParser(properties);
      Diary diary = diaryParser.parse(inputPath.getFileName().toString(), inputStream);

      printStream.println(DOCTYPE_HTML);
      SimpleElementWriter writer = new SimpleElementWriter(printStream);
      new DiaryTemplate(diary, properties, writer).render();

    } catch (IOException | XMLStreamException ex) {
      throw new RuntimeException("Could not process file:" + inputPath, ex);
    }
  }
}
