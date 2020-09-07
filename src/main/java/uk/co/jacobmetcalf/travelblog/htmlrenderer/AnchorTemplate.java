package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.PhrasingContentChoice;
import uk.co.jacobmetcalf.travelblog.model.Anchor;
public class AnchorTemplate {

  /**
   * Adds an href to a paragraph or span.
   *
   * We cannot use HtmlFlow's partial templates here because they rely on wrapping elements
   * in "div" elements which are not valid children of a "p" element.
   *
   * Assumes that it is already in a dynamic element.
   */
  @SuppressWarnings("rawtypes")
  public static <Z extends PhrasingContentChoice<Z, P>, P extends Element> void add(
      final Z e, final Anchor anchor) {
    // @formatter:off
    e.a()
        .attrTarget("_new")
        .attrHref(anchor.getRef())
        .text(anchor.getText())
      .__();
    // @formatter:on
  }
}
