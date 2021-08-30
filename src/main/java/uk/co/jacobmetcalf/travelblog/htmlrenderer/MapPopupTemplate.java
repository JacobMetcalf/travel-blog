package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Div;
import uk.co.jacobmetcalf.travelblog.model.LocatableWithSummary;

/**
 * Renders a small popup on the map.
 * This can include a thumbnail image, a description and a link.
 */
public class MapPopupTemplate {

  private final LocatableWithSummary locatableWithSummary;

  public MapPopupTemplate(final LocatableWithSummary locatableWithSummary) {
    this.locatableWithSummary = locatableWithSummary;
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
          // TODO: Add date
        .b().text(locatableWithSummary.getTitle()).__()
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
    return locatableWithSummary.getThumb()
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
    return locatableWithSummary.getThumb()
        .map(t -> parent
            .span()
              .text(t)
            .__())
        .orElse(parent);
  }

  private Div<Body<?>> addLink(final Div<Body<?>> parent) {
    return parent
        .br().__()
        .br().__()
        .span()
          .a()
            .attrClass("float-right")
            .attrHref(locatableWithSummary.getCanonicalUrl())
            .b()
              .text("view &gt;&gt;")
            .__()
          .__()
        .__();
  }
}
