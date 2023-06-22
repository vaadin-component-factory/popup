package com.vaadin.componentfactory;

import com.vaadin.flow.component.shared.ThemeVariant;

/**
 * Set of theme variants applicable for {@code vcf-popup} component.
 */
public enum PopupVariant implements ThemeVariant {
    //@formatter:off
    LUMO_POINTER_ARROW("pointer-arrow");
    //@formatter:on

    private final String variant;

    PopupVariant(String variant) {
        this.variant = variant;
    }

    /**
     * Gets the variant name.
     *
     * @return variant name
     */
    public String getVariantName() {
        return variant;
    }
}
