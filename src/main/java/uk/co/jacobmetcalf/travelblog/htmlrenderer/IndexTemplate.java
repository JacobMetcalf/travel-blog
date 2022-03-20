package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static uk.co.jacobmetcalf.travelblog.htmlrenderer.ThirdPartyResources.BOOTSTRAP_CSS;
import static uk.co.jacobmetcalf.travelblog.htmlrenderer.ThirdPartyResources.CSS_INTEGRITY;
import static uk.co.jacobmetcalf.travelblog.htmlrenderer.ThirdPartyResources.ICON_FONT_CSS;
import static uk.co.jacobmetcalf.travelblog.htmlrenderer.ThirdPartyResources.JQUERY_JS;

import java.util.Set;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.ElementVisitor;
import org.xmlet.htmlapifaster.EnumCrossoriginCrossOriginType;
import org.xmlet.htmlapifaster.EnumRelType;
import org.xmlet.htmlapifaster.EnumTypeContentType;
import org.xmlet.htmlapifaster.EnumTypeScriptType;
import org.xmlet.htmlapifaster.Html;
import uk.co.jacobmetcalf.travelblog.executor.Properties;
import uk.co.jacobmetcalf.travelblog.model.IndexEntry;
import uk.co.jacobmetcalf.travelblog.model.ImmutableLocation;
import uk.co.jacobmetcalf.travelblog.model.Location;

/**
 * Template for rendering the index page.
 */
@SuppressWarnings("UnusedReturnValue") // We use unused return type to syntactically ensure tags closed
public class IndexTemplate {

  private static final String TITLE = "Anna and Jacob Metcalf's Travels";

  private static final String ADDITIONAL_STYLES =
      """
          #map {height: 200px;}
          @media (min-width : 768px) { #map { height: 300px; }}
          @media (min-width : 992px) { #map { height: 400px; }}""";

  private final Set<IndexEntry> references;
  private final Properties properties;
  private final NavbarTemplate headerTemplate;
  private final MapTemplate mapTemplate;
  private final ElementVisitor elementVisitor;

  public IndexTemplate(final Set<IndexEntry> references,
      final Properties properties,
      final ElementVisitor elementVisitor) {

    // TODO: Fix header.
    this.references = references;
    this.properties = properties;
    Location centre = ImmutableLocation.builder().location("World").latitude(30).longitude(15).zoom(2).build();
    this.headerTemplate = new NavbarTemplate(TITLE);
    this.mapTemplate = new MapTemplate(centre, properties);
    this.elementVisitor = elementVisitor;
  }

  public void render() {
    // @formatter:off
    new Html<>(elementVisitor)
        .attrLang("en")
        .head()
          .title().text(TITLE).__()
          .meta().attrCharset("utf-8").__()
          .meta().attrName("viewport")
            .attrContent("width=device-width, initial-scale=1, shrink-to-fit=no")
          .__()
          .link().addAttr("rel", "canonical")
            .attrHref(properties.get(Properties.Key.CANONICAL_URL).orElseThrow())
          .__()
          .link().attrRel(EnumRelType.STYLESHEET).attrHref(BOOTSTRAP_CSS)
            .addAttr("integrity", CSS_INTEGRITY)
            .attrCrossorigin(EnumCrossoriginCrossOriginType.ANONYMOUS).__()
          .link().attrRel(EnumRelType.STYLESHEET).attrHref(ICON_FONT_CSS).__()
          .style().attrType(EnumTypeContentType.TEXT_CSS).text(ADDITIONAL_STYLES).__()
          .script().attrType(EnumTypeScriptType.TEXT_JAVASCRIPT).attrSrc(JQUERY_JS).__()
          .script().attrType(EnumTypeScriptType.TEXT_JAVASCRIPT).attrSrc("diary_functions.js").__()
        .__() //head
        .body()
          .div().attrId("content").attrClass("container")
            .of(headerTemplate::add)
            .of(this::addMapDiv)
            .of(this::addEntry)
            .of(mapTemplate::addFooterScript)
        .__()
      .__();
      // @formatter:on
  }

  /**
   * Add the divider in which the map will be drawn.
   */
  public <T extends Element<T,?>> void addMapDiv(final Div<T> parent) {
    // @formatter:off
    parent
      .div().attrClass("clearfix py-1")
        .div().attrId("map").attrClass("mb-1").__()
      .__();
    // @formatter:on
  }

  public <T extends Element<T,?>> Div<T> addEntry(final Div<T> parent) {
    return parent.of(p -> references.forEach(r -> addEntry(p, r)));
  }

  public <T extends Element<T,?>> Div<T> addEntry(final Div<T> parent,
      final IndexEntry entry) {
    
    String popupHtml = new MapPopupTemplate(entry).render();
    return parent
        // @formatter:off
        .script().of(s -> MapTemplate.addLocation(s, entry, popupHtml)).__()
        .a()
          .attrClass("btn btn-outline-primary mr-1 mb-1")
          .addAttr("role", "button")
          .attrHref(entry.getRelativeUrl())
          .text(entry.getLocation())
        .__();
        // @formatter:on
  }
}
