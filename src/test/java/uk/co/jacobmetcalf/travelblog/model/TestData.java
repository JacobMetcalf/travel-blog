package uk.co.jacobmetcalf.travelblog.model;

import java.time.LocalDate;
import java.util.stream.Stream;
import uk.co.jacobmetcalf.travelblog.model.Image.Position;
import uk.co.jacobmetcalf.travelblog.xmlparser.AnchorPullParser;

/**
 * Test data for tests to avoid repeating
 */
public final class TestData {
  private TestData() {}

  public static final LocalDate JUL_19 = LocalDate.of(2018, 7, 19);

  public static final Text TEXT_1 = ImmutableText.builder().text("Start").build();
  public static final Text TEXT_2 = ImmutableText.builder().text(" middle ").build();
  public static final Text TEXT_3 = ImmutableText.builder().text(" end.").build();

  public static final Anchor WIKI_LINK = ImmutableAnchor.builder()
      .ref(AnchorPullParser.WIKIPEDIA_BASE + "Quito").text("Quito").build();

  public static final Anchor HREF_1 = ImmutableAnchor.builder()
      .ref("https://www.royensoc.co.uk/identifying-insects")
      .text("Royal Entomological Society")
      .build();

  public static final Location QUITO = ImmutableLocation.builder()
      .location("Quito")
      .province("Pichincha")
      .country("Ecuador")
      .latitude(-0.2181)
      .longitude(-78.5084)
      .build();

  public static final Location ECUADOR = ImmutableLocation.builder()
      .country("Ecuador")
      .build();

  public static final Image IMAGE_1 = ImmutableImage.builder()
      .from(QUITO)
      .src("ecuador001")
      .title("Imabura with snow from hotel")
      .position(Position.LEFT)
      .build();

  public static final Image IMAGE_2 = ImmutableImage.builder()
      .from(QUITO)
      .src("ecuador002")
      .title("Church and monastery of San Fransisco")
      .position(Position.RIGHT)
      .build();

  public static final Entry ENTRY_1 = ImmutableEntry.builder()
      .from(TestData.QUITO)
      .date(TestData.JUL_19)
      .addParagraphs(ImmutableParagraph.builder()
          .addParts(TestData.TEXT_1).build())
      .addParagraphs(ImmutableParagraph.builder()
          .addParts(TestData.TEXT_3).build())
      .build();

  public static final Route ROUTE_1 = ImmutableRoute.builder()
      .addPoints(
          ImmutablePoint.builder().latitude(10d).longitude(-40d).build(),
          ImmutablePoint.builder().latitude(12d).longitude(-42d).build())
      .build();

  public static final Diary DIARY_NO_ENTRIES = ImmutableDiary.builder()
      .from(QUITO)
      .title("Test title")
      .thumb("test.gif")
      .filename("test.xml")
      .entriesAndRoutes(Stream.empty())
      .build();

  public static final Book BOOK_1 = ImmutableBook.builder()
      .title("Test book")
      .isin("123ABC")
      .build();
}
