package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import com.google.common.base.Preconditions;
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
import uk.co.jacobmetcalf.travelblog.model.Diary;
import uk.co.jacobmetcalf.travelblog.model.Entry;
import uk.co.jacobmetcalf.travelblog.model.EntryOrRoute;
import uk.co.jacobmetcalf.travelblog.model.Route;

public class DiaryTemplate {

  private static final String BOOTSTRAP_CSS =
      "https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css";

  private static final String CSS_INTEGRITY =
      "sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z";

  private static final String ICON_FONT_CSS =
      "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css";

  private static final String ADDITIONAL_STYLES =
      ".clear-left { clear: left; }\n"
          + ".clear-right { clear: right; }\n"
          + "#map { height: 200px; }\n"
          + "@media (min-width : 768px) { #map { height: 300px; }}\n"
          + "@media (min-width : 992px) { #map { height: 400px; }}";

  private static final String SLIDESHOW_CSS =
      "https://cdnjs.cloudflare.com/ajax/libs/magnific-popup.js/1.1.0/magnific-popup.min.css";

  private static final String JQUERY_JS =
      "https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js";

  private static final String SLIDESHOW_JS =
      "https://cdnjs.cloudflare.com/ajax/libs/magnific-popup.js/1.1.0/jquery.magnific-popup.min.js";

  private static final String HAMMER_JS =
      "https://cdnjs.cloudflare.com/ajax/libs/hammer.js/2.0.8/hammer.min.js";

  private final Diary diary;
  private final String canonicalUrl;
  private final NavbarTemplate headerTemplate;
  private final FooterTemplate footerTemplate;
  private final MapTemplate mapTemplate;
  private final BookTemplate bookTemplate;
  private final EntryTemplate entryTemplate = new EntryTemplate();
  private final ElementVisitor elementVisitor;

  public DiaryTemplate(final Diary diary,
      final String googleApiKey,
      final String amazonAssociatesKey,
      final String linkedInId,
      final String canonicalUrl,
      final ElementVisitor elementVisitor) {
    this.diary = diary;
    this.canonicalUrl = canonicalUrl;
    this.headerTemplate = new NavbarTemplate(diary);
    this.footerTemplate = new FooterTemplate(linkedInId);
    this.mapTemplate = new MapTemplate(diary, googleApiKey);
    this.bookTemplate = new BookTemplate(diary.getBooks(), amazonAssociatesKey);
    this.elementVisitor = elementVisitor;

    // TODO: Move keys to properties and make more flexible
  }

  public void render() {
    // @formatter:off
    new Html<>(elementVisitor)
        .attrLang("en")
        .head()
          .title().text(diary.getTitle()).__()
          .meta().attrCharset("utf-8").__()
          .link().addAttr("rel", "canonical").attrHref(getCanonicalPath()).__()
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
            .of(addEntriesAndRoutes(diary.getEntriesAndRoutes()))
            .of(mapTemplate::addFooterScript)
          .__()
          .of(footerTemplate::add)
        .__()
      .__();
      // @formatter:on
  }

  /**
   * Add the divider in which the map will be drawn
   */
  public <T extends Element<T,?>> Div<T> addMapDiv(final Div<T> parent) {
    // @formatter:off
    return parent
        .div().attrClass("clearfix row ml-1 py-1")
        .div().attrId("map").attrClass("col-lg-11 mb-1").__()
        .div().attrId("books").attrClass("col-lg-1").of(bookTemplate::add).__()
        .__();
    // @formatter:on
  }

  private <T extends Element<T,?>> Consumer<Div<T>> addEntriesAndRoutes(
      final Stream<EntryOrRoute> entriesAndRoutes) {

    return d -> {
        final EntryOrRoute.Visitor visitor = new EntryOrRouteVisitor<>(d);
        entriesAndRoutes.forEach(e -> e.visit(visitor));
    };
  }

  /**
   * Google uses the canonical path to figure out what the preferred URL is when
   * presented with different URLs pointing to the path.
   */
  private String getCanonicalPath() {
    String[] parts = diary.getFilename().split("\\.");
    Preconditions.checkArgument(parts.length == 2, "Path cannot have more than one dot in");
    return canonicalUrl + "/" + parts[0] + ".html";
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
