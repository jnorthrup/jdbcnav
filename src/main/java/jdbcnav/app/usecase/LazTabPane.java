package jdbcnav.app.usecase;

import org.dom4j.Element;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public class LazTabPane extends LazAbstractElement {
    public LazTabPane(UseCase data) {
        super(data);
    }

    public Element getElement(Element actorTabSliderElement) {
        return actorTabSliderElement.addElement("tabpane").addAttribute("text", getBeanName(data));
    }


}
