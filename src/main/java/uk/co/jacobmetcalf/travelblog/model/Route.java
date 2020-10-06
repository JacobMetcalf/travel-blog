package uk.co.jacobmetcalf.travelblog.model;

import java.util.List;
import org.immutables.value.Value;

/**
 * An entry in a diary, consists of a number of paragraphs
 */
@ImmutableStyle
@Value.Immutable
public abstract class Route {
  public abstract List<Point> getPoints();
}
