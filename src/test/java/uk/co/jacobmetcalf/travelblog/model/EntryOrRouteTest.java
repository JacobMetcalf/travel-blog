package uk.co.jacobmetcalf.travelblog.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EntryOrRouteTest {

  @Test
  public void can_only_add_entry_or_route() {
    Assertions.assertThrows(IllegalStateException.class,
        () -> ImmutableEntryOrRoute.builder()
            .entry(TestData.ENTRY_1)
            .route(TestData.ROUTE_1)
            .build());

    Assertions.assertThrows(IllegalStateException.class,
        () -> ImmutableEntryOrRoute.builder()
            .route(TestData.ROUTE_1)
            .entry(TestData.ENTRY_1)
            .build());
  }
}
