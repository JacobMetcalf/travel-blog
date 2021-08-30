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
import uk.co.jacobmetcalf.travelblog.model.DiarySummary;
import uk.co.jacobmetcalf.travelblog.model.ImmutableLocation;
import uk.co.jacobmetcalf.travelblog.model.LocatableWithSummary;
import uk.co.jacobmetcalf.travelblog.model.Location;
import uk.co.jacobmetcalf.travelblog.model.Properties;

public class IndexTemplate {

  private static final String TITLE = "Anna and Jacob Metcalf's Travels";

  private static final String ADDITIONAL_STYLES =
      """
          #map {height: 200px;}
          @media (min-width : 768px) { #map { height: 300px; }}
          @media (min-width : 992px) { #map { height: 400px; }}""";

  private final Set<DiarySummary> references;
  private final Properties properties;
  private final NavbarTemplate headerTemplate;
  private final MapTemplate mapTemplate;
  private final ElementVisitor elementVisitor;

  public IndexTemplate(final Set<DiarySummary> references,
      final Properties properties,
      final ElementVisitor elementVisitor) {
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
          .script().attrType(EnumTypeScriptType.TEXT_JAVASCRIPT).attrSrc("../diary_functions.js").__()
        .__() //head
        .body()
          .div().attrId("content").attrClass("container")
            .of(headerTemplate::add)
            .of(this::addMapDiv)
            .of(this::addReferences)
            .of(mapTemplate::addFooterScript)
        .__()
      .__();
      // @formatter:on
  }

  /**
   * Add the divider in which the map will be drawn.
   */
  public <T extends Element<T,?>> Div<T> addMapDiv(final Div<T> parent) {
    // @formatter:off
    return parent
      .div().attrClass("clearfix py-1")
        .div().attrId("map").attrClass("mb-1").__()
      .__();
    // @formatter:on
  }

  public <T extends Element<T,?>> Div<T> addReferences(final Div<T> parent) {
    return parent.of(p -> references.forEach(r -> addReference(p, r)));
  }

  public <T extends Element<T,?>> Div<T> addReference(final Div<T> parent,
      final LocatableWithSummary reference) {
    
    // TODO : Algo for link name north, south etc. Write tests
    String popupHtml = new MapPopupTemplate(reference).render();
    return parent
        .script().of(s -> MapTemplate.addLocation(s, reference, popupHtml)).__()
        .a()
          .attrClass("btn btn-outline-primary mr-1")
          .addAttr("role", "button")
          .attrHref(reference.getCanonicalUrl())
          .text(reference.getCountry().orElseThrow())
        .__();
  }
}
