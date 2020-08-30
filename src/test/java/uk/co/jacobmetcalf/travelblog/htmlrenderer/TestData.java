package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import java.time.LocalDate;
import uk.co.jacobmetcalf.travelblog.model.Anchor;
import uk.co.jacobmetcalf.travelblog.model.Image;
import uk.co.jacobmetcalf.travelblog.model.Image.Position;
import uk.co.jacobmetcalf.travelblog.model.ImmutableAnchor;
import uk.co.jacobmetcalf.travelblog.model.ImmutableImage;
import uk.co.jacobmetcalf.travelblog.model.ImmutableLocation;
import uk.co.jacobmetcalf.travelblog.model.ImmutableText;
import uk.co.jacobmetcalf.travelblog.model.Location;
import uk.co.jacobmetcalf.travelblog.model.Text;
import uk.co.jacobmetcalf.travelblog.xmlparser.AnchorParser;
import uk.co.jacobmetcalf.travelblog.xmlparser.TestUtil;

/**
 * Test data for tests to avoid repeating
 */
public final class TestData {
  private TestData() {}

  public static final LocalDate JUL_19 = LocalDate.of(2018, 7, 19);
  public static final LocalDate JUL_20 = LocalDate.of(2018, 7, 20);

  public static final Text TEXT_1 = ImmutableText.builder().text("Start").build();
  public static final Text TEXT_2 = ImmutableText.builder().text(" middle ").build();
  public static final Text TEXT_3 = ImmutableText.builder().text(" end.").build();

  public static final Anchor WIKI_LINK = ImmutableAnchor.builder()
      .ref(AnchorParser.WIKIPEDIA_BASE + "Quito").text("Quito").build();

  public static final Anchor HREF_1 = ImmutableAnchor.builder()
      .ref("https://www.royensoc.co.uk/identifying-insects")
      .text("Royal Entomological Society")
      .build();

  public static final Location LOCATION_1 = ImmutableLocation.builder()
      .location("Quito")
      .province("Pichincha")
      .country("Ecuador")
      .latitude(-0.2181)
      .longitude(-78.5084)
      .build();

  public static final Image IMAGE_1 = ImmutableImage.builder()
      .src("ecuador001")
      .title("Imabura with snow from hotel")
      .position(Position.LEFT)
      .location(TestUtil.QUITO)
      .build();

  public static final Image IMAGE_2 = ImmutableImage.builder()
      .src("ecuador002")
      .title("Church and monastery of San Fransisc")
      .position(Position.RIGHT)
      .location(TestUtil.QUITO)
      .build();
}
