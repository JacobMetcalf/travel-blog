package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.format.DateTimeFormatter;
import org.xmlet.htmlapifaster.B;
import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Div;
import uk.co.jacobmetcalf.travelblog.model.IndexEntry;

/**
 * Templates which renders a small popup on the map.
 * This can include a thumbnail image, a description and a link.
 */
@SuppressWarnings("UnusedReturnValue") // We use unused return type to syntactically ensure tags closed
public class MapPopupTemplate {

  private final static DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("MMMM yyyy");

  private final IndexEntry indexEntry;

  public MapPopupTemplate(final IndexEntry indexEntry) {
    this.indexEntry = indexEntry;
  }

  /**
   * Renders key details as some html which can be popped up on a Google map.
   */
  public String render() {

    try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteStream)) {
      SimpleElementWriter writer = new SimpleElementWriter(printStream);

      // @formatter:off
      new Div<Body<?>>(writer)
        .attrStyle("width:250px;height:150px")
        .of(this::addThumb)
        .b()
          .of(this::addDate)
          .text(indexEntry.getTitle())
        .__()
        .br().__()
        .of(this::addSummary)
          .br().__()
          .br().__()
          .of(this::addLink);
      // @formatter:on

      return byteStream.toString();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  private Div<Body<?>> addThumb(final Div<Body<?>> parent) {
    return indexEntry.getRelativeThumbUrl()
        .map(i -> parent
            // @formatter:off
            .img()
              .attrClass("float-left pr-1")
              .attrSrc(i)
            .__())
            // @formatter:on
        .orElse(parent);
  }

  private Div<Body<?>> addSummary(final Div<Body<?>> parent) {
    return indexEntry.getSummary()
        .map(t -> parent
            // @formatter:off
            .span()
              .text(t)
            .__())
            // @formatter:on
        .orElse(parent);
  }

  private B<Div<Body<?>>> addDate(final B<Div<Body<?>>> parent) {
    return indexEntry.getDate()
        .map(d -> parent.text(DATE_FORMAT.format(d) + ": ")).orElse(parent);
  }

  private Div<Body<?>> addLink(final Div<Body<?>> parent) {
    return parent
        .br().__()
        .br().__()
        .span()
          .a()
            .attrClass("float-right")
            .attrHref(indexEntry.getRelativeUrl())
            .b()
              .text("view &gt;&gt;")
            .__()
          .__()
        .__();
  }
}
