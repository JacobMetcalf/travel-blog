package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.P;

public class TestHelper {
  public static <Z extends Element<Z,?>> String renderInDiv(final Consumer<Div<Z>> unitAdd) {

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    SimpleElementWriter writer = new SimpleElementWriter(new PrintStream(out));
    new Div<Z>(writer).of(unitAdd).__();
    return out.toString();
  }

  public static <Z extends Element<Z,?>> String renderInP(final Consumer<P<Z>> unitAdd) {

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    SimpleElementWriter writer = new SimpleElementWriter(new PrintStream(out));
    new P<Z>(writer).of(unitAdd).__();
    return out.toString();
  }
}
