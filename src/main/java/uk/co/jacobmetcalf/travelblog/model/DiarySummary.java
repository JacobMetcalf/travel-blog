package uk.co.jacobmetcalf.travelblog.model;

import com.google.common.collect.ComparisonChain;
import org.immutables.value.Value;

@ImmutableStyle
@Value.Immutable
public abstract class DiarySummary implements LocatableWithSummary, Comparable<DiarySummary>{

  /**
   * Natural order is to sort alphabetically by country then province.
   */
  @Override
  public int compareTo(DiarySummary other) {
    return ComparisonChain.start()
        .compare(this.getCountry().orElse(""), other.getCountry().orElse(""))
        .compare(this.getProvince().orElse(""), other.getProvince().orElse(""))
        .compare(this.getCanonicalUrl(), other.getCanonicalUrl())
        .result();
  }
}
