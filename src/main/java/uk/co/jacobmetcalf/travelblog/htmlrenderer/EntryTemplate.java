package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import htmlflow.DynamicHtml;
import htmlflow.HtmlView;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import uk.co.jacobmetcalf.travelblog.model.Entry;

public class EntryTemplate {

  private final static DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy");

  public final static HtmlView<Entry> template =
      DynamicHtml.view(EntryTemplate::entryTemplate).setIndented(false);

  private static void entryTemplate(final DynamicHtml<Entry> view, final Entry entry) {
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
            .of(s -> view.addPartial(LocationTemplate.fullyQualifiedTemplate, entry.getLocation()))
          .__()
        .__()
      .__()
      .of(d -> entry.getParagraphs().forEach(
          p -> view.addPartial(ParagraphTemplate.template, p)))
    .__();
    // @formatter:on
  }

  static String formatDate(final LocalDate date) {
    return DATE_FORMAT.format(date);
  }
}
