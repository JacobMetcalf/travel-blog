package uk.co.jacobmetcalf.travelblog.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.htmlrenderer.ImmutableProperties;
import uk.co.jacobmetcalf.travelblog.model.Properties.Key;

public class PropertiesTest {

  @Test
  public void get_property() {
    ImmutableProperties unit = ImmutableProperties.builder()
        .putValue(Key.GOOGLE_API_KEY, "Google key")
        .putValue(Key.LINKED_IN, "My linked in")
        .build();

    assertThat(unit.get(Key.GOOGLE_API_KEY).isPresent(), equalTo(true));
    assertThat(unit.get(Key.GOOGLE_API_KEY).get(), equalTo("Google key"));

    assertThat(unit.get(Key.LINKED_IN).isPresent(), equalTo(true));
    assertThat(unit.get(Key.LINKED_IN).get(), equalTo("My linked in"));

    assertThat(unit.get(Key.TWITTER).isPresent(), equalTo(false));
  }
}
