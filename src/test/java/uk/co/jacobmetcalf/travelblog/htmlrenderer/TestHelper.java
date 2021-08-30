package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;
import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.P;
import org.xmlet.htmlapifaster.Script;
import org.xmlet.htmlapifaster.Ul;

public class TestHelper {
  public static <Z extends Element<Z,?>> String renderInDiv(final Consumer<Div<Z>> unitAdd) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    SimpleElementWriter writer = new SimpleElementWriter(new PrintStream(out));
    new Div<Z>(writer).of(unitAdd).__();
    return out.toString();
  }

  public static <Z extends Element<Z,?>> String renderInUl(final Consumer<Ul<Z>> unitAdd) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    SimpleElementWriter writer = new SimpleElementWriter(new PrintStream(out));
    new Ul<Z>(writer).of(unitAdd).__();
    return out.toString();
  }

  public static <Z extends Element<Z,?>> String renderInP(final Consumer<P<Z>> unitAdd) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    SimpleElementWriter writer = new SimpleElementWriter(new PrintStream(out));
    new P<Z>(writer).of(unitAdd).__();
    return out.toString();
  }

  public static <Z extends Element<Z,?>> String renderInBody(final Consumer<Body<Z>> unitAdd) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    SimpleElementWriter writer = new SimpleElementWriter(new PrintStream(out));
    new Body<Z>(writer).of(unitAdd).__();
    return out.toString();
  }

  public static <Z extends Element<Z,?>> String renderInScript(final Consumer<Script<Z>> unitAdd) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    SimpleElementWriter writer = new SimpleElementWriter(new PrintStream(out));
    new Script<Z>(writer).of(unitAdd).__();
    return out.toString();
  }
}
