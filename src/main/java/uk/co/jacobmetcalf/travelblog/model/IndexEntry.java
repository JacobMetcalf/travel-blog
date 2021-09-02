package uk.co.jacobmetcalf.travelblog.model;

import com.google.common.collect.ComparisonChain;
import java.time.LocalDate;
import java.util.Optional;
import org.immutables.value.Value;

/**
 * A collection of index entries is used to generate the index page.
 */
@ImmutableStyle
@Value.Immutable
public abstract class IndexEntry implements Locatable, Comparable<IndexEntry>{

  public abstract Optional<LocalDate> getDate();
  public abstract String getTitle();
  public abstract Optional<String> getSummary();
  public abstract String getRelativeUrl();
  public abstract Optional<String> getRelativeThumbUrl();

  /**
   * Natural order is to sort alphabetically by country then province.
   */
  @Override
  public int compareTo(IndexEntry other) {
    return ComparisonChain.start()
        .compare(this.getCountry().orElse(""), other.getCountry().orElse(""))
        .compare(this.getProvince().orElse(""), other.getProvince().orElse(""))
        .compare(this.getDate().orElse(LocalDate.MIN), other.getDate().orElse(LocalDate.MIN))
        .compare(this.getRelativeUrl(), other.getRelativeUrl())
        .result();
  }

  // Extend builder for common attributes
  interface Builder extends Locatable.Builder {}
}
