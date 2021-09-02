package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static uk.co.jacobmetcalf.travelblog.htmlrenderer.ThirdPartyResources.BOOTSTRAP_CSS;
import static uk.co.jacobmetcalf.travelblog.htmlrenderer.ThirdPartyResources.CSS_INTEGRITY;
import static uk.co.jacobmetcalf.travelblog.htmlrenderer.ThirdPartyResources.HAMMER_JS;
import static uk.co.jacobmetcalf.travelblog.htmlrenderer.ThirdPartyResources.ICON_FONT_CSS;
import static uk.co.jacobmetcalf.travelblog.htmlrenderer.ThirdPartyResources.JQUERY_JS;
import static uk.co.jacobmetcalf.travelblog.htmlrenderer.ThirdPartyResources.SLIDESHOW_CSS;
import static uk.co.jacobmetcalf.travelblog.htmlrenderer.ThirdPartyResources.SLIDESHOW_JS;

import java.util.function.Consumer;
import java.util.stream.Stream;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.ElementVisitor;
import org.xmlet.htmlapifaster.EnumCrossoriginCrossOriginType;
import org.xmlet.htmlapifaster.EnumRelType;
import org.xmlet.htmlapifaster.EnumTypeContentType;
import org.xmlet.htmlapifaster.EnumTypeScriptType;
import org.xmlet.htmlapifaster.Html;
import uk.co.jacobmetcalf.travelblog.executor.Properties;
import uk.co.jacobmetcalf.travelblog.model.Diary;
import uk.co.jacobmetcalf.travelblog.model.Entry;
import uk.co.jacobmetcalf.travelblog.model.EntryOrRoute;
import uk.co.jacobmetcalf.travelblog.model.Route;

/**
 * Template which outputs the head and body.
 */
@SuppressWarnings("UnusedReturnValue") // We use unused return type to syntactically ensure tags closed
public class DiaryTemplate {

  private static final String ADDITIONAL_STYLES =
      """
          .clear-left { clear: left; }
          .clear-right { clear: right; }
          #map { height: 200px; }
          @media (min-width : 768px) { #map { height: 300px; }}
          @media (min-width : 992px) { #map { height: 400px; }}""";

  private final Diary diary;
  private final NavbarTemplate headerTemplate;
  private final FooterTemplate footerTemplate;
  private final MapTemplate mapTemplate;
  private final BookTemplate bookTemplate;
  private final EntryTemplate entryTemplate = new EntryTemplate();
  private final ElementVisitor elementVisitor;

  public DiaryTemplate(final Diary diary,
      final Properties properties,
      final ElementVisitor elementVisitor) {
    this.diary = diary;
    this.headerTemplate = new NavbarTemplate(diary.getTitle(), diary.getNavigationAnchors());
    this.footerTemplate = new FooterTemplate(diary.getNavigationAnchors(), properties);
    this.mapTemplate = new MapTemplate(diary, properties);
    this.bookTemplate = new BookTemplate(diary.getBooks(), properties);
    this.elementVisitor = elementVisitor;
  }

  public void render() {
    // @formatter:off
    new Html<>(elementVisitor)
        .attrLang("en")
        .head()
          .title().text(diary.getTitle()).__()
          .meta().attrCharset("utf-8").__()
          .meta().attrName("viewport")
            .attrContent("width=device-width, initial-scale=1, shrink-to-fit=no")
          .__()
          .link().addAttr("rel", "canonical").attrHref(diary.getCanonicalUrl()).__()
          .link().attrRel(EnumRelType.STYLESHEET).attrHref(BOOTSTRAP_CSS)
            .addAttr("integrity", CSS_INTEGRITY)
            .attrCrossorigin(EnumCrossoriginCrossOriginType.ANONYMOUS).__()
          .link().attrRel(EnumRelType.STYLESHEET).attrHref(ICON_FONT_CSS).__()
          .link().attrRel(EnumRelType.STYLESHEET).attrHref(SLIDESHOW_CSS).__()
          .style().attrType(EnumTypeContentType.TEXT_CSS).text(ADDITIONAL_STYLES).__()
          .script().attrType(EnumTypeScriptType.TEXT_JAVASCRIPT).attrSrc(JQUERY_JS).__()
          .script().attrType(EnumTypeScriptType.TEXT_JAVASCRIPT).attrSrc(SLIDESHOW_JS).__()
          .script().attrType(EnumTypeScriptType.TEXT_JAVASCRIPT).attrSrc(HAMMER_JS).__()
          .script().attrType(EnumTypeScriptType.TEXT_JAVASCRIPT).attrSrc("../diary_functions.js").__()
        .__() //head
        .body()
          .div().attrId("content").attrClass("container")
            .of(headerTemplate::add)
            .of(this::addMapDiv)
            .of(addEntriesAndRoutes(diary.getEntriesAndRoutes().stream()))
            .of(mapTemplate::addFooterScript)
          .__()
          .of(footerTemplate::add)
        .__()
      .__();
      // @formatter:on
  }

  /**
   * Add the divider in which the map will be drawn. Book element is optional.
   */
  public <T extends Element<T,?>> Div<T> addMapDiv(final Div<T> parent) {
    // @formatter:off
    if (bookTemplate.hasBooks()) {
      return parent
          .div().attrClass("clearfix row ml-1 py-1")
            .div().attrId("map").attrClass("col-lg-11 mb-1").__()
            .div().attrId("books").attrClass("col-lg-1")
              .of(bookTemplate::add)
            .__()
          .__();
    } else {
      return parent
        .div().attrClass("clearfix row ml-1 py-1")
          .div().attrId("map").attrClass("col-lg-11 mb-1").__()
        .__();
    }
    // @formatter:on
  }

  private <T extends Element<T,?>> Consumer<Div<T>> addEntriesAndRoutes(
      final Stream<EntryOrRoute> entriesAndRoutes) {

    return d -> {
        final EntryOrRoute.Visitor visitor = new EntryOrRouteVisitor<>(d);
        entriesAndRoutes.forEach(e -> e.visit(visitor));
    };
  }

  private class EntryOrRouteVisitor<T extends Element<T,?>> implements EntryOrRoute.Visitor {

    private final Div<T> parent;

    private EntryOrRouteVisitor(final Div<T> parent) {
      this.parent = parent;
    }

    @Override
    public void visit(final Entry entry) {
      entryTemplate.add(parent, entry);
    }

    @Override
    public void visit(final Route route) {
      mapTemplate.addRoute(parent, route);
    }
  }
}
