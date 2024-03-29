package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import com.google.common.base.Preconditions;
import java.util.stream.Collectors;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.EnumTypeScriptType;
import org.xmlet.htmlapifaster.Script;
import uk.co.jacobmetcalf.travelblog.executor.Properties;
import uk.co.jacobmetcalf.travelblog.model.Locatable;
import uk.co.jacobmetcalf.travelblog.model.Route;

/**
 * Template for a google map. A little bit gnarly as creating the map requires
 * a bit of javascript.
 */
@SuppressWarnings("UnusedReturnValue") // We use unused return type to syntactically ensure tags closed
public class MapTemplate {

  private final Locatable centre;
  private final String apiKey;

  public MapTemplate(final Locatable centre, final Properties properties) {
    Preconditions.checkArgument(centre.hasCoords(), "Map centre must have coordinates");
    this.centre = centre;
    this.apiKey = properties.get(Properties.Key.GOOGLE_API_KEY).orElseThrow();
  }

  /**
   * Javascript to add the route to the collection.
   */
  public <T extends Element<T,?>> void addRoute(final Div<T> parent, final Route route) {

    String routeJson = route.getPoints().stream()
        .map(p -> "{lat:" + p.getLatitude() + ",lng:" + p.getLongitude() + "}")
        .collect(Collectors.joining(","));

    parent
        .script().attrType(EnumTypeScriptType.TEXT_JAVASCRIPT)
          .text("routes.push({path:[" + routeJson
              + "],geodesic: true,strokeColor:'#FF0000',strokeOpacity:1.0,strokeWeight:3});\n")
        .__();
  }

  /**
   * Used by the location template to add a location to a javascript
   * dictionary which will be rendered as points on the map.
   */
  public static <T extends Element<T,?>> Script<T> addLocation(
      final Script<T> script, final Locatable locatable, @Nullable String popupHtml) {

    if (!locatable.hasCoords() || locatable.getLocation().isEmpty()) {
      return script;
    }

    String addLocationEntryJs = "locations[\"" + locatable.getLocation()
        + "\"] = {lat:" + locatable.getLatitude().orElse(0d)
        + ",lng:" + locatable.getLongitude().orElse(0d);

    if (popupHtml != null) {
      addLocationEntryJs += ",popup:`" + popupHtml + "`";
    }
    addLocationEntryJs += "};";

    return script.attrType(EnumTypeScriptType.TEXT_JAVASCRIPT).text(addLocationEntryJs);
  }

  /**
   * Adds a call back to centre the map, initialise the google api and then
   * call the initMap() function which plots all the routes.
   */
  public <T extends Element<T,?>> Div<T> addFooterScript(final Div<T> parent) {
    return parent
        .script().attrType(EnumTypeScriptType.TEXT_JAVASCRIPT).text(
            "centre = {lat:" + centre.getLatitude().orElse(0d)
                + ",lng:" + centre.getLongitude().orElse(0d) +"}; "
                + "zoom = " + centre.getZoom() + ";").__()
        .script().attrType(EnumTypeScriptType.TEXT_JAVASCRIPT)
          .attrAsync(true)
          .attrDefer(true)
          .attrSrc("https://maps.googleapis.com/maps/api/js?key=" + apiKey + "&callback=initMap")
        .__();
  }
}
