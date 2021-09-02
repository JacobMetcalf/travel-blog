package uk.co.jacobmetcalf.travelblog.model;

import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.collect.ImmutableSortedSet;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class IndexEntryTest {

  @Test
  public void natural_set_orders_by_country() {
    ImmutableSortedSet<IndexEntry> result = ImmutableSortedSet.<IndexEntry>naturalOrder()
        .add(createDiarySummary("Greece", "Athens"),
            createDiarySummary("Germany", "Bavaria"))
        .build();

    assertThat(result.first().getCountry().orElseThrow(), Matchers.equalTo("Germany"));
  }

  @Test
  public void natural_set_orders_by_province() {
    ImmutableSortedSet<IndexEntry> result = ImmutableSortedSet.<IndexEntry>naturalOrder()
        .add(createDiarySummary("Germany", "Thuringia"),
            createDiarySummary("Germany", "Bavaria"))
        .build();

    assertThat(result.first().getProvince().orElseThrow(), Matchers.equalTo("Bavaria"));
  }

  private IndexEntry createDiarySummary(String country, String province) {
    return ImmutableIndexEntry.builder().country(country).province(province)
        .relativeUrl("germany/diary.html").title("Test")
        .relativeThumbUrl("germany/images/test.jpg").build();
  }
}
