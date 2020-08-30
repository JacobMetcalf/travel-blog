package uk.co.jacobmetcalf.travelblog.xmlparser;

import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.Image.Position;
import uk.co.jacobmetcalf.travelblog.model.ImmutableImage;
import uk.co.jacobmetcalf.travelblog.model.ImmutableText;
import uk.co.jacobmetcalf.travelblog.model.Paragraph;

public class ParagraphParserTest {

  public final ParagraphParser unit = new ParagraphParser();

  @Test
  public void can_parse_paragraph_with_text() {
    String inputXml = "<paragraph>Some text</paragraph>";
    Paragraph actual = TestUtil.tryParse(inputXml, unit, TestUtil.quitoAsBuilder());

    MatcherAssert.assertThat(actual.getParts(),
        Matchers.contains(equalTo(ImmutableText.builder().text("Some text").build())));
  }

  @Test
  public void ignores_irrelevant_whitespace() {
    String inputXml = "  \n<paragraph>Some text</paragraph>\n\t ";
    Paragraph actual = TestUtil.tryParse(inputXml, unit, TestUtil.quitoAsBuilder());

    MatcherAssert.assertThat(actual.getParts(),
        Matchers.contains(equalTo(ImmutableText.builder().text("Some text").build())));
  }

  @Test
  public void can_parse_paragraph_with_entity_refs() {
    String inputXml = "<paragraph>Compa&#241;&#237;a de Jes&#250;s</paragraph>";
    Paragraph actual = TestUtil.tryParse(inputXml, unit, TestUtil.quitoAsBuilder());

    MatcherAssert.assertThat(actual.getParts(),
        Matchers.contains(
            equalTo(ImmutableText.builder().text("Compa").build()),
            equalTo(ImmutableText.builder().text("ñ").build()),
            equalTo(ImmutableText.builder().text("í").build()),
            equalTo(ImmutableText.builder().text("a de Jes").build()),
            equalTo(ImmutableText.builder().text("ú").build()),
            equalTo(ImmutableText.builder().text("s").build())
        ));
  }

  @Test
  public void can_parse_paragraph_with_image() {
    String inputXml = "<paragraph>Text before <image src=\"ecuador001\" position=\"left\" "
        + "title=\"An image\"/>\ntext after</paragraph>";
    Paragraph actual = TestUtil.tryParse(inputXml, unit, TestUtil.quitoAsBuilder());

    MatcherAssert.assertThat(actual.getImages(),
        Matchers.contains(
          equalTo(ImmutableImage.builder().title("An image")
              .src("ecuador001")
              .position(Position.LEFT)
              .location(TestUtil.QUITO)
              .build())));

    MatcherAssert.assertThat(actual.getParts(),
        Matchers.contains(equalTo(ImmutableText.builder().text("Text before ").build()),
            equalTo(ImmutableText.builder().text("\ntext after").build())));


  }
}