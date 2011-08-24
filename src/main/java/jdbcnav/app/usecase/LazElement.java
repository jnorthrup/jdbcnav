package jdbcnav.app.usecase;

import org.dom4j.Element;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public interface LazElement {
    Element getElement(Element actorTabSliderElement);

    String getBeanName(Object data);
}
