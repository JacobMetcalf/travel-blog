package uk.co.jacobmetcalf.travelblog.model;

import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.collect.ImmutableSortedSet;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class DiarySummaryTest {

  @Test
  public void natural_set_orders_by_country() {
    ImmutableSortedSet<DiarySummary> result = ImmutableSortedSet.<DiarySummary>naturalOrder()
        .add(createDiarySummary("Greece", "Athens"),
            createDiarySummary("Germany", "Bavaria"))
        .build();

    assertThat(result.first().getCountry().orElseThrow(), Matchers.equalTo("Germany"));
  }

  @Test
  public void natural_set_orders_by_province() {
    ImmutableSortedSet<DiarySummary> result = ImmutableSortedSet.<DiarySummary>naturalOrder()
        .add(createDiarySummary("Germany", "Thuringia"),
            createDiarySummary("Germany", "Bavaria"))
        .build();

    assertThat(result.first().getProvince().orElseThrow(), Matchers.equalTo("Bavaria"));
  }

  private DiarySummary createDiarySummary(String country, String province) {
    return ImmutableDiarySummary.builder().country(country).province(province)
        .canonicalUrl("https://www.test.com/file.html").title("Test").thumb("test").build();
  }
}
