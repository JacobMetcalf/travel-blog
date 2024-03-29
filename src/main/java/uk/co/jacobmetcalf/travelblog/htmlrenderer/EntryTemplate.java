package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.Span;
import uk.co.jacobmetcalf.travelblog.model.Entry;
import uk.co.jacobmetcalf.travelblog.model.Locatable;

/**
 * Template which displays a diary entry for a day.
 */
@SuppressWarnings("UnusedReturnValue") // We use unused return type to syntactically ensure tags closed
public class EntryTemplate {

  private final static DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy");

  private final LocationTemplate locationTemplate = new LocationTemplate();
  private final ParagraphTemplate paragraphTemplate = new ParagraphTemplate();

  public <T extends Element<T,?>> Div<T> add(final Div<T> parent, final Entry entry) {
    // @formatter:off
    return parent
      .div()
        .attrClass("clearfix")
        .div()
          .attrClass("card card-body my-3")
          .h6()
            .attrClass("card-subtitle")
            .of(h -> h.text(formatDate(entry.getDate())))
            .br().attrClass("d-sm-none").__()
            .span()
              .attrClass("float-md-right")
              .of(addLocation(entry))
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
  private <X extends Element<X,?>> Consumer<Span<X>> addLocation(final Locatable locatable) {
    return s -> locationTemplate.add(s, locatable, true);
  }

  static String formatDate(final @NonNull LocalDate date) {
    return DATE_FORMAT.format(date);
  }
}
