package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.P;
import uk.co.jacobmetcalf.travelblog.model.Anchor;

/**
 * Template which displays a hypertext link.
 */
@SuppressWarnings("UnusedReturnValue") // We use unused return type to syntactically ensure tags closed
public class AnchorTemplate {

  /**
   * Adds a href to a paragraph.
   */
  public <T extends Element<T,?>> P<T> add(final P<T> parent, final Anchor anchor) {
    // @formatter:off
    return parent.a()
        .attrTarget("_new")
        .attrHref(anchor.getRef())
        .text(anchor.getText())
      .__();
    // @formatter:on
  }
}
