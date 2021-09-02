package uk.co.jacobmetcalf.travelblog.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class EntriesAndRoutesTest {

  final EntriesAndRoutes unit = new EntriesAndRoutes(Stream.of(
      ImmutableEntryOrRoute.builder().entry(TestData.ENTRY_1).build(),
      ImmutableEntryOrRoute.builder().route(TestData.ROUTE_1).build()));

  @Test
  public void captures_first_date() {
    unit.stream().forEach(e -> {});
    assertThat(unit.getStartDate().orElseThrow(), equalTo(TestData.JUL_19));
  }

  @Test
  public void throws_if_not_first_consumed() {
    assertThrows(IllegalStateException.class, () -> unit.getStartDate());
  }
}
