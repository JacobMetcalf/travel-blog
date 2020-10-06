package uk.co.jacobmetcalf.travelblog.model;

import com.google.common.base.Preconditions;
import java.util.Optional;
import org.immutables.value.Value;

/**
 * Container for either a route OR an entry.
 *
 * This rather awkward construct allows us to stream across the vast bulk of the diary
 * only pulling one route or entry at a time.
 */
@ImmutableStyle
@Value.Immutable
public abstract class EntryOrRoute {
  public abstract Optional<Entry> getEntry();
  public abstract Optional<Route> getRoute();

  public void visit(final Visitor visitor) {
    // Validation check ensures only one of these is true
    getEntry().ifPresent(visitor::visit);
    getRoute().ifPresent(visitor::visit);
  }

  public interface Visitor {
    void visit(Entry entry);
    void visit(Route route);
  }

  @Value.Check
  protected void check() {
    Preconditions.checkState((getEntry().isPresent() && getRoute().isEmpty())
            || (getEntry().isEmpty() && getRoute().isPresent()),
        "Exactly one of route or entry must be present");
  }
}
