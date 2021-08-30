package uk.co.jacobmetcalf.travelblog.model;

import java.util.Optional;

/**
 * Extends locatable to add summary information. Diary and DiaryReference both implement.
 */
public interface LocatableWithSummary extends Locatable {
  String getTitle();
  String getCanonicalUrl();
  Optional<String> getSummary();
  Optional<String> getThumb();

  // Extend builder for common attributes
  interface Builder extends Locatable.Builder {}
}
