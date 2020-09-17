package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import org.xmlet.htmlapifaster.P;
import uk.co.jacobmetcalf.travelblog.model.Anchor;
public class AnchorTemplate {

  /**
   * Adds an href to a paragraph .
   */
  public void add(final P<?> parent, final Anchor anchor) {
    // @formatter:off
    parent.a()
        .attrTarget("_new")
        .attrHref(anchor.getRef())
        .text(anchor.getText())
      .__();
    // @formatter:on
  }
}
