package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import java.util.EnumMap;
import java.util.function.BiConsumer;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

public class AttributeParser<B> {

  private final ImmutableMap<AttributeToken, BiConsumer<B,Attribute>> attributeMap;
  private final ElementToken elementToken;

  private AttributeParser(final ElementToken elementToken, final ImmutableMap<AttributeToken, BiConsumer<B,Attribute>> attributeMap) {
    this.attributeMap = attributeMap;
    this.elementToken = elementToken;
  }

  /**
   * Iterates through the attributes of the element calling lambdas on the builder, which
   * is passed in.
   * @param builder Builder which consumers operate on.
   * @param startElement Element to iterate over attributes of.
   */
  public B parse(final B builder, final StartElement startElement) {
    Streams.stream(startElement.getAttributes())
        .forEach( a -> {
          try {
            AttributeToken attributeToken = AttributeToken.fromAttributeName(a);
            attributeMap.getOrDefault(attributeToken, this::throwError).accept(builder, a);

          } catch (IllegalArgumentException ex) {
            throwError(builder, a);
          }
        });
    return builder;
  }

  public void throwError(final B builder, final Attribute attribute) {
    throw new IllegalStateException("Unexpected attribute: " + attribute
        + " of " + elementToken + ", expecting: " + Joiner.on("|").join(attributeMap.keySet()));
  }

  public static <B> Builder<B> builder() {
    return new Builder<>(new EnumMap<>(AttributeToken.class));
  }

  /**
   * Builder
   */
  public static class Builder<B> {

    private final EnumMap<AttributeToken,BiConsumer<B,Attribute>> map;
    private ElementToken elementToken = null;

    private Builder(final EnumMap<AttributeToken,BiConsumer<B,Attribute>> map) {
      this.map = map;
    }

    public Builder<B> put(final AttributeToken key, final BiConsumer<B,Attribute> action) {
      map.put(key, action);
      return this;
    }

    public Builder<B> withElementToken(final ElementToken elementToken) {
      this.elementToken = elementToken;
      return this;
    }

    public AttributeParser<B> build() {
      Preconditions.checkState(elementToken != null, "ElementToken is mandatory");
      return new AttributeParser<>(elementToken, Maps.immutableEnumMap(map));
    }
  }
}
