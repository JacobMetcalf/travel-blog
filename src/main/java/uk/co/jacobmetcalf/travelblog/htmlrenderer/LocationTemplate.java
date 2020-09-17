package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import com.google.common.base.Joiner;
import java.util.function.Consumer;
import org.xmlet.htmlapifaster.A;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.PhrasingContentChoice;
import uk.co.jacobmetcalf.travelblog.model.Location;

/**
 * Template for adding the HTMl for a location link to a
 * @see org.xmlet.htmlapifaster.PhrasingContentChoice, typically a span or a p element.
 */
public class LocationTemplate {
  private static final Joiner LOCATION_JOINER = Joiner.on(", ").skipNulls();

  /**
   * Adds a location link to a paragraph or span.
   */
  public <E extends PhrasingContentChoice<E,?>> void add(final E parent,
      final Location location, boolean fullyQualified) {
    // @formatter:off

    E result = parent
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

  private <E extends Element<E,?>> Consumer<A<E>> ifNoWikiLinkToMap(final Location location) {
    return a -> location.getWiki()
        .ifPresentOrElse(a::attrHref,
            () -> a.attrHref("#").attrOnclick(centreOnMap(location)));
  }

  private <E extends PhrasingContentChoice<E,?>> void ifWikiAddGlobeIcon(final E parent, final Location location) {
    if (location.getWiki().isPresent()) {
        parent.text("&nbsp;")
          .a()
            .attrOnclick(centreOnMap(location))
            .attrHref("#")
            .i().attrClass("fa fa-globe").__()
          .__();
    }
  }

  private String centreOnMap(Location location) {
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
