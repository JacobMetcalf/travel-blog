package uk.co.jacobmetcalf.travelblog.model;

import java.util.Map;
import java.util.Optional;
import org.immutables.value.Value;

/**
 * Properties provide all the necessary API keys and social media
 * identities.
 */
@ImmutableStyle
@Value.Immutable
public abstract class Properties {
  public enum Key {
    GOOGLE_API_KEY,
    CANONICAL_URL,
    AMAZON_ASSOCIATES_ID,
    LINKED_IN,
    FACEBOOK,
    TWITTER,
    GITHUB
  }

  /**
   * @return The entire property array.
   */
  public abstract Map<Key, String> value();

  /**
   * Gets the value of the key
   */
  public Optional<String> get(final Key key) {
    return Optional.ofNullable(value().get(key));
  }

  public static Properties of(Key key, String value) {
    return ImmutableProperties.builder().putValue(key, value).build();
  }

  public static Properties of(Key key1, String value1, Key key2, String value2) {
    return ImmutableProperties.builder()
        .putValue(key1, value1)
        .putValue(key2, value2)
        .build();
  }
}
