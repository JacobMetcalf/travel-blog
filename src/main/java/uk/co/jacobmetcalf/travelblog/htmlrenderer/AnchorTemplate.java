package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.P;
import uk.co.jacobmetcalf.travelblog.model.Anchor;
public class AnchorTemplate {

  /**
   * Adds a href to a paragraph.
   */
  public <T extends Element<T,?>> void add(final P<T> parent, final Anchor anchor) {
    // @formatter:off
    parent.a()
        .attrTarget("_new")
        .attrHref(anchor.getRef())
        .text(anchor.getText())
      .__();
    // @formatter:on
  }
}
