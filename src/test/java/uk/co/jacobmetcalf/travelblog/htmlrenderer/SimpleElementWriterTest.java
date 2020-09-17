package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.xmlet.htmlapifaster.A;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.ElementVisitor;
import org.xmlet.htmlapifaster.Img;

public class SimpleElementWriterTest {
  @ParameterizedTest(name="{0}")
  @MethodSource("testArgsProvider")
  public void can_render_html(final String testName,
      final Consumer<ElementVisitor> elementConsumer, final String expected) {

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    SimpleElementWriter unit = new SimpleElementWriter(new PrintStream(out));
    elementConsumer.accept(unit);

    assertEquals(expected, out.toString());
  }

  public static Stream<Arguments> testArgsProvider() {

    return Stream.of(

        arguments("Simple void element",
            (Consumer<ElementVisitor>)(v) -> new Img<>(v).attrSrc("source").__(),
            "<img src=\"source\">"),
        arguments("Element with text and attribute",
            (Consumer<ElementVisitor>)(v) -> new A<>(v).attrHref("to").text("link").__(),
            "<a href=\"to\">link</a>"),
        arguments("Nested elements",
            (Consumer<ElementVisitor>)(v) -> new Div<>(v).p().text("hello").__().__(),
            "<div><p>hello</p></div>"),
        arguments("Multiple nested elements",
            (Consumer<ElementVisitor>)(v) -> new Div<>(v).p().text("para1").__()
                .p().text("para2").__().__(),
            "<div><p>para1</p><p>para2</p></div>"),
        arguments("Multiple nested void elements",
            (Consumer<ElementVisitor>)(v) -> new Div<>(v).br().__().br().__().__(),
            "<div><br><br></div>"),
        arguments("Multiple nested empty non-void elements",
            (Consumer<ElementVisitor>)(v) -> new Div<>(v).i().__().i().__().__(),
            "<div><i></i><i></i></div>"),
        arguments("Element with comment",
            (Consumer<ElementVisitor>)(v) -> new Div<>(v).comment("a comment").__(),
            "<div><!-- a comment --></div>")
    );
  }
}
