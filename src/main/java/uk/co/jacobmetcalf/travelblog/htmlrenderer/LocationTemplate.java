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
  public <E extends PhrasingContentChoice<E,?>> E add(final E parent,
      final Location location, boolean fullyQualified) {

    // @formatter:off
    E result = parent
        .a().attrId(location.getLocation().orElseThrow()).__()
        .a()
          .attrTarget("_new")
          .of(ifNoWikiLinkToMap(location))
          .of(a -> a.text(fullyQualified ?
            formatLocation(location) : location.getLocation().get()))
        .__()
        .script().of(s -> MapTemplate.addLocation(s, location)).__();
    // @formatter:on
    ifWikiAddGlobeIcon(result, location);
    return result;
  }

  private <E extends Element<E,?>> Consumer<A<E>> ifNoWikiLinkToMap(final Location location) {
    return a -> location.getWiki()
        .ifPresentOrElse(a::attrHref, () -> centreOnMap(a, location));
  }

  private <E extends PhrasingContentChoice<E,?>> void ifWikiAddGlobeIcon(final E parent, final Location location) {
    if (location.getWiki().isPresent()) {
        parent.text("&nbsp;")
          .a()
            .of(a -> centreOnMap(a, location))
            .i().attrClass("fa fa-globe").__()
          .__();
    }
  }

  private <T extends Element<T,?>> A<T> centreOnMap(final A<T> parent, final Location location) {

    if (location.getLongitude().isEmpty() || location.getLatitude().isEmpty()) {
      return parent;
    }
    return parent.attrOnclick("map.setCenter({lat:" + location.getLatitude().get()
        + ",lng:" + location.getLongitude().get()
        + "});map.setZoom(" + location.getZoom()
        + ");").attrHref("#");
  }

	public static String formatLocation(final Location location) {
	  return LOCATION_JOINER.join(location.getLocation().orElse(null),
        location.getProvince().orElse(null), location.getCountry().orElse(null));
  }
}
