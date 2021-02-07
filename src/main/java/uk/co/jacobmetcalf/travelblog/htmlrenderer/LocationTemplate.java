package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import com.google.common.base.Joiner;
import java.util.function.Consumer;
import org.xmlet.htmlapifaster.A;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.PhrasingContentChoice;
import uk.co.jacobmetcalf.travelblog.model.Locatable;

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
      final Locatable locatable, boolean fullyQualified) {

    // @formatter:off
    E result = parent
        .a().attrId(locatable.getLocation().orElseThrow()).__()
        .a()
          .of(ifNoWikiLinkToMap(locatable))
          .of(a -> a.text(fullyQualified ?
              formatLocatable(locatable) : locatable.getLocation().get()))
        .__()
        .script().of(s -> MapTemplate.addLocation(s, locatable)).__();
    // @formatter:on
    ifWikiAddGlobeIcon(result, locatable);
    return result;
  }

  private <E extends Element<E,?>> Consumer<A<E>> ifNoWikiLinkToMap(final Locatable locatable) {
    return a -> locatable.getWiki()
        .ifPresentOrElse(l -> a.attrTarget("_new").attrHref(l),
            () -> centreOnMap(a, locatable));
  }

  private <E extends PhrasingContentChoice<E,?>> void ifWikiAddGlobeIcon(final E parent, final Locatable locatable) {
    if (locatable.getWiki().isPresent()) {
        parent.text("&nbsp;")
          .a()
            .of(a -> centreOnMap(a, locatable))
            .i().attrClass("fa fa-globe").__()
          .__();
    }
  }

  private <T extends Element<T,?>> A<T> centreOnMap(final A<T> parent, final Locatable locatable) {

    if (locatable.getLongitude().isEmpty() || locatable.getLatitude().isEmpty()) {
      return parent;
    }
    return parent.attrOnclick("map.setCenter({lat:" + locatable.getLatitude().get()
        + ",lng:" + locatable.getLongitude().get()
        + "});map.setZoom(" + locatable.getZoom()
        + ");").attrHref("#");
  }

	public static String formatLocatable(final Locatable locatable) {
	  return LOCATION_JOINER.join(locatable.getLocation().orElse(null),
        locatable.getProvince().orElse(null),
        locatable.getCountry().orElse(null));
  }
}
