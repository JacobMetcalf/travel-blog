package uk.co.jacobmetcalf.travelblog.executor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class PropertiesTest {

  @Test
  public void get_property() {
    ImmutableProperties unit = ImmutableProperties.builder()
        .putValue(Properties.Key.GOOGLE_API_KEY, "Google key")
        .putValue(Properties.Key.LINKED_IN, "My linked in")
        .build();

    assertThat(unit.get(Properties.Key.GOOGLE_API_KEY).isPresent(), equalTo(true));
    assertThat(unit.get(Properties.Key.GOOGLE_API_KEY).get(), equalTo("Google key"));

    assertThat(unit.get(Properties.Key.LINKED_IN).isPresent(), equalTo(true));
    assertThat(unit.get(Properties.Key.LINKED_IN).get(), equalTo("My linked in"));

    assertThat(unit.get(Properties.Key.TWITTER).isPresent(), equalTo(false));
  }
}
