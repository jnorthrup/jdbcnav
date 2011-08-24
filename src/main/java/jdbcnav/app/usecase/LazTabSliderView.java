package jdbcnav.app.usecase;

import org.dom4j.Element;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public class LazTabSliderView<T> extends LazAbstractElement {

    public LazTabSliderView(Object data) {
        super(data);
    }

    public Element getElement(Element actorTabSliderElement) {
        return actorTabSliderElement.addElement("tabelement").addAttribute("text", getBeanName(data));
    }


}
