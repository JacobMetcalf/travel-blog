package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import htmlflow.DynamicHtml;
import htmlflow.HtmlView;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.checkerframework.checker.nullness.qual.NonNull;
import uk.co.jacobmetcalf.travelblog.model.Entry;

public class EntryTemplate {

  private final static DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy");

  public final static HtmlView<Entry> template =
      DynamicHtml.view(EntryTemplate::entryTemplate).setIndented(true);

  private static void entryTemplate(final @NonNull DynamicHtml<Entry> view, final Entry entry) {
    // @formatter:off
    view.div()
      .attrClass("clearfix p-t-1")
      .div()
        .attrClass("card card-block")
        .h6()
          .attrClass("card-subtitle")
          .dynamic(h -> h.text(formatDate(entry.getDate())))
          .br()
            .attrClass("hidden-sm-up")
          .__()
          .span()
            .attrClass("pull-md-right")
            .dynamic(s -> LocationTemplate.addLocation(s, entry.getLocation(), true))
          .__()
        .__()
      .__()
      .dynamic(d -> entry.getParagraphs().forEach(
          p -> view.addPartial(ParagraphTemplate.template, p)))
    .__();
    // @formatter:on
  }

  static String formatDate(final @NonNull LocalDate date) {
    return DATE_FORMAT.format(date);
  }
}
