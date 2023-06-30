package com.vaadin.componentfactory;

import java.util.Arrays;

/**
 * Popup alignment options.
 */
public enum PopupAlignment {

    /**
     * This will align the Popup to the center of the target element.
     */
    CENTER("center");

    private final String propertyValue;

    PopupAlignment(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public static PopupAlignment fromPropertyValue(String propertyValue) {
        return Arrays.stream(PopupAlignment.values())
                .filter(popupAlignment -> popupAlignment.getPropertyValue().equals(propertyValue))
                .findFirst().orElse(null);
    }

}
