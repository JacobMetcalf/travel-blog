package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import com.google.common.base.Joiner;
import htmlflow.DynamicHtml;
import htmlflow.HtmlView;
import java.util.function.Consumer;
import org.xmlet.htmlapifaster.A;
import org.xmlet.htmlapifaster.Div;
import uk.co.jacobmetcalf.travelblog.model.Location;

public class LocationTemplate {
  private static final Joiner LOCATION_JOINER = Joiner.on(", ").skipNulls();

  public final static HtmlView<Location> template =
      DynamicHtml.view(LocationTemplate::locationTemplate).setIndented(false);

  public final static HtmlView<Location> fullyQualifiedTemplate =
      DynamicHtml.view(LocationTemplate::fullyQualifiedLocationTemplate).setIndented(false);

  // TODO: There ought to be  more elegant way of doing this than having two templates

  private static void locationTemplate(final DynamicHtml<Location> view, final Location location) {
    // @formatter:off
    view.div()
        // Target for links
        .a()
          .attrId(location.getLocation())
        .__()
        .a()
          .attrTarget("_new")
          .dynamic(ifNoWikiLinkToMap(location))
          .dynamic(a -> a.text(location.getLocation()))
        .__()
        .dynamic(ifWikiLinkToMap(location));
    // @formatter:on
  }

  private static void fullyQualifiedLocationTemplate(final DynamicHtml<Location> view,
      final Location location) {
    // @formatter:off
    view.div()
        // Target for links
        .a()
          .attrId(location.getLocation())
        .__()
        .a()
          .attrTarget("_new")
          .dynamic(ifNoWikiLinkToMap(location))
          .dynamic(a -> a.text(formatLocation(location)))
        .__()
        .dynamic(ifWikiLinkToMap(location));
    // @formatter:on
  }

  @SuppressWarnings("rawtypes")
  private static Consumer<A<Div<HtmlView>>> ifNoWikiLinkToMap(Location location) {
    return a -> location.getWiki()
        .ifPresentOrElse(a::attrHref,
            () -> a.attrHref("#").attrOnclick(centreOnMap(location)));
  }

  @SuppressWarnings("rawtypes")
  private static Consumer<Div<HtmlView>> ifWikiLinkToMap(Location location) {
    return d -> {
      if (location.getWiki().isPresent()) {
        d.a()
          .attrOnclick(centreOnMap(location))
          .attrHref("#")
          .img()
            .attrClass("fa fa-globe")
          .__()
        .__();
      }
    };
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
