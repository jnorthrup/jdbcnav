package jdbcnav.app.usecase;

import org.dom4j.Element;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public class LazTabsView<T> extends LazAbstractElement<T> {
    public LazTabsView(T data) {
        super(data);
    }

    public Element getElement(Element actorTabSliderElement) {
        return actorTabSliderElement.addElement("tabs");
    }
}
