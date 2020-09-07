package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import htmlflow.DynamicHtml;
import htmlflow.HtmlView;
import org.xmlet.htmlapifaster.EnumRelType;
import org.xmlet.htmlapifaster.EnumTypeContentType;
import uk.co.jacobmetcalf.travelblog.model.Diary;

public class DiaryTemplate {

  private static final String CSS_URL =
      "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.4/css/bootstrap.min.css";
  private static final String ICON_FONT_URL =
      "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css";

  public final static HtmlView<Diary> template =
      DynamicHtml.view(DiaryTemplate::pageTemplate).setIndented(true);

  private static void pageTemplate(final DynamicHtml<Diary> view, final Diary diary) {
    // @formatter:off
    view.html()
        .attrLang("en")
        .head()
          .title().text(diary.getTitle()).__()
          .meta().attrCharset("utf-8").__()
          .link().attrHref(CSS_URL).attrRel(EnumRelType.STYLESHEET).__()
          .link().attrHref(ICON_FONT_URL).attrRel(EnumRelType.STYLESHEET).__()
          .style().attrType(EnumTypeContentType.TEXT_CSS).text(
              ".clear-left { clear: left; }\n"
              + ".clear-right { clear: right; }\n"
              + "#map { height: 200px; }\n"
              + "@media (min-width : 768px) { #map { height: 300px; }}\n"
              + "@media (min-width : 992px) { #map { height: 400px; }}")
            .__()
        .__() //head
        .body()
          .div() //<div id="content" class="container">
            .attrId("content").attrClass("container")
            .of(b -> diary.getEntries().forEach(
              e -> view.addPartial(EntryTemplate.template, e)))
          .__()
        .__()
      .__();
    // @formatter:on
  }
}
