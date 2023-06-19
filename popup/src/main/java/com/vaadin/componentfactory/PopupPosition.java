package com.vaadin.componentfactory;

/**
 * Popup positioning options.
 */
public enum PopupPosition {

    /**
     * Position the popup to the bottom of the target element.
     * If there is not enough space below the target element, then
     * the popup will be repositioned to the top of the target element.
     */
    BOTTOM("bottom"),

    /**
     * Position the popup to the right (or in RTL text direction environment
     * to the left) of the target element. If there is not enough space on the
     * right side of the target element, then the Popup will be repositioned to
     * the left of the target element.
     */
    END("end");

    private final String propertyValue;

    PopupPosition(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getPropertyValue() {
        return propertyValue;
    }
}
