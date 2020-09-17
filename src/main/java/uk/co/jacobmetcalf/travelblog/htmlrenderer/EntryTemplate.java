package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.Span;
import uk.co.jacobmetcalf.travelblog.model.Entry;
import uk.co.jacobmetcalf.travelblog.model.Location;

public class EntryTemplate {

  private final static DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy");

  private final LocationTemplate locationTemplate = new LocationTemplate();
  private final ParagraphTemplate paragraphTemplate = new ParagraphTemplate();

  public <Z extends Element<Z,?>> void add(final Div<Z> parent, final Entry entry) {
    // @formatter:off
    parent.div()
        .attrClass("clearfix p-t-1")
        .div()
          .attrClass("card card-block")
          .h6()
            .attrClass("card-subtitle")
            .dynamic(h -> h.text(formatDate(entry.getDate())))
            .br().attrClass("hidden-sm-up").__()
            .span()
              .attrClass("pull-md-right")
              .of(addLocation(entry.getLocation()))
            .__()
        .__()
      .__()
      .of(d -> entry.getParagraphs().forEach(p -> paragraphTemplate.add(d, p)))
    .__();
    // @formatter:on
  }

  /**
   * Since the location element is heavily nested use a generic method
   */
  private <X extends Element<X,?>> Consumer<Span<X>> addLocation(final Location location) {
    return s -> locationTemplate.add(s, location, true);
  }

  static String formatDate(final @NonNull LocalDate date) {
    return DATE_FORMAT.format(date);
  }
}
