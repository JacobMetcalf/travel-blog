package uk.co.jacobmetcalf.travelblog;

import com.beust.jcommander.Parameter;
import uk.co.jacobmetcalf.travelblog.executor.Executor;
import uk.co.jacobmetcalf.travelblog.executor.ImmutableProperties;
import uk.co.jacobmetcalf.travelblog.executor.Properties;

/**
 * Target for JCommander, places command line arguments in a {@link Properties}
 * then calls the {@link Executor}.
 */
public class Command {

  private final ImmutableProperties.Builder properties = ImmutableProperties.builder();
  private final Executor executor = new Executor();

  private String directory = null;
  private boolean recursive = false;
  private boolean createIndex = false;
  private boolean help = false;

  @Parameter(description = "Directory to scan for xml files", required = true)
  public void setDirectory(String directory) {
    this.directory = directory;
  }

  @Parameter(names = {"--url", "-u"},
      description = "Canonical (i.e. the one you want Google to index) url", required = true)
  public void setCanonicalUrl(String canonicalUrl) {
    properties.putValue(Properties.Key.CANONICAL_URL, canonicalUrl);
  }

  @Parameter(names = {"--googlekey", "-g"}, description = "Google api key for maps (mandatory)", required = true)
  public void setGoogleKey(String googleKey) {
    properties.putValue(Properties.Key.GOOGLE_API_KEY, googleKey);
  }

  @Parameter(names = {"--amazonkey", "-a"}, description = "Amazon associates key for selling books")
  public void setAmazonKey(String amazonKey) {
    properties.putValue(Properties.Key.AMAZON_ASSOCIATES_ID, amazonKey);
  }

  @Parameter(names = {"--linkedin", "-l"}, description = "Linked in id")
  public void setLinkedInId(String linkedIn) {
    properties.putValue(Properties.Key.LINKED_IN, linkedIn);
  }

  @Parameter(names = {"--facebook", "-f"}, description = "Facebook id")
  public void setFacebook(String facebook) {
    properties.putValue(Properties.Key.FACEBOOK, facebook);
  }

  @Parameter(names = {"--twitter", "-t"}, description = "Twitter id")
  public void setTwitter(String twitter) {
    properties.putValue(Properties.Key.TWITTER, twitter);
  }

  @Parameter(names = {"--github", "-b"}, description = "GitHub account")
  public void setGitHub(String gitHub) {
    properties.putValue(Properties.Key.GITHUB, gitHub);
  }

  @Parameter(names = {"--recursive", "-r"}, description = "Whether to recursively search sub-directories of the given directory")
  public void setRecursive(boolean recursive) {
    this.recursive = recursive;
  }

  @Parameter(names = { "--create-index", "-i" }, description = "Whether to create an index file.")
  public void setCreateIndex(boolean createIndex) {
    this.createIndex = createIndex;
  }

  @Parameter(names = { "--help", "-h" }, help = true)
  public void setHelp(boolean help) {
    this.help = help;
  }

  public boolean isHelp() {
      return help;
    }

  public void execute() {
    executor.process(directory, recursive, createIndex, properties.build());
  }
}
