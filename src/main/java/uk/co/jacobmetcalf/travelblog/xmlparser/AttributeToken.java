package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Preconditions;
import javax.xml.stream.events.Attribute;

public enum AttributeToken {
  COUNTRY,
  PROVINCE,
  LOCATION,
  LATITUDE,
  LONGITUDE,
  DATE,
  POSITION,
  WIKI,
  ZOOM,
  REF, // We need to deprecate this
  HREF,
  TITLE,
  SRC;

  public static AttributeToken fromAttributeName(final Attribute attribute) {

    String name = attribute.getName().getLocalPart();
    Preconditions.checkArgument(name.toLowerCase().equals(name),
        "Attribute names should be lower case: " + name);

    return AttributeToken.valueOf(name.toUpperCase());
  }
}
