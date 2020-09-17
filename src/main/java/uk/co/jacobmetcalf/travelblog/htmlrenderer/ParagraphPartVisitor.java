package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.P;
import uk.co.jacobmetcalf.travelblog.model.Anchor;
import uk.co.jacobmetcalf.travelblog.model.Location;
import uk.co.jacobmetcalf.travelblog.model.ParagraphPart;
import uk.co.jacobmetcalf.travelblog.model.Text;

public class ParagraphPartVisitor<Z extends Element<Z,?>> implements ParagraphPart.Visitor {

  private final AnchorTemplate anchorTemplate = new AnchorTemplate();
  private final LocationTemplate locationTemplate = new LocationTemplate();
  private final P<Z> paragraphElement;

  ParagraphPartVisitor(final @NonNull P<Z> paragraphElement) {
    this.paragraphElement = paragraphElement;
  }

  @Override
  public void visit(final @NonNull Anchor anchor) {
    anchorTemplate.add(paragraphElement, anchor);
  }

  @Override
  public void visit(final @NonNull Location location) {
    locationTemplate.add(paragraphElement, location, false);
  }

  @Override
  public void visit(final @NonNull Text text) {
    paragraphElement.text(text.getText());
  }
}
