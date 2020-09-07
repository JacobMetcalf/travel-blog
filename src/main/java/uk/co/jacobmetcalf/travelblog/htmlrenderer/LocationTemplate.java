package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import com.google.common.base.Joiner;
import java.util.function.Consumer;
import org.xmlet.htmlapifaster.A;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.PhrasingContentChoice;
import uk.co.jacobmetcalf.travelblog.model.Location;

@SuppressWarnings("rawtypes")
public class LocationTemplate {
  private static final Joiner LOCATION_JOINER = Joiner.on(", ").skipNulls();

  /**
   * Adds a location link to a paragraph or span.
   *
   * We cannot use HtmlFlow's partial templates here because they rely on wrapping elements
   * in "div" elements which are not valid children of a "p" element.
   *
   * Assumes that it is already in a dynamic element.
   */
  public static <Z extends PhrasingContentChoice<Z, P>, P extends Element> void addLocation(
      final Z p, final Location location, boolean fullyQualified) {
    // @formatter:off
    Z result = p
        .a().attrId(location.getLocation()).__()
        .a()
          .attrTarget("_new")
          .of(ifNoWikiLinkToMap(location))
          .of(a -> a.text(fullyQualified ?
            formatLocation(location) : location.getLocation()))
        .__();
    // @formatter:on
    ifWikiAddGlobeIcon(result, location);
  }

  private static <Z extends Element> Consumer<A<Z>> ifNoWikiLinkToMap(Location location) {
    return a -> location.getWiki()
        .ifPresentOrElse(a::attrHref,
            () -> a.attrHref("#").attrOnclick(centreOnMap(location)));
  }

  public static <Z extends PhrasingContentChoice<Z, P>, P extends Element>
      void ifWikiAddGlobeIcon(final Z p, final Location location) {
    if (location.getWiki().isPresent()) {
        p.text("&nbsp;").a()
          .attrOnclick(centreOnMap(location))
          .attrHref("#")
          .i().attrClass("fa fa-globe").__()
        .__();
    }
  }

  private static String centreOnMap(Location location) {
    return "map.setCenter({lat:" + location.getLatitude()
        + ",lng:" + location.getLongitude()
        + "});map.setZoom(" + location.getZoom()
        + ");";
  }

	public static String formatLocation(final Location location) {
	  return LOCATION_JOINER.join(location.getLocation(),
        location.getProvince(), location.getCountry());
  }
}
