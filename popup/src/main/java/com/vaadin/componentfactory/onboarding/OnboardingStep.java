package com.vaadin.componentfactory.onboarding;

import com.vaadin.componentfactory.Popup;
import com.vaadin.componentfactory.PopupAlignment;
import com.vaadin.componentfactory.PopupPosition;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.function.SerializableConsumer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines one step of the "Onboarding walkthrough". See {@link Onboarding} for more information.
 * <p>
 * The {@code OnboardingStep} basically defines what the Popup should show ({@link #setContent(String)}
 * or {@link #setContent(Component)}) in the given step and how. You can further
 * customize the displayed Popup using {@link OnboardingStep#addBeforePopupShownListener(SerializableConsumer)}
 * <p>
 * The Popup may (but doesn't have to) point to an element - use {@link #setTargetElement(Component)} for it.
 */
public class OnboardingStep implements Serializable {

    private Component targetElement;
    private PopupPosition position = PopupPosition.END;
    private PopupAlignment alignment = PopupAlignment.CENTER;
    private Component content;
    private String header;
    private final List<SerializableConsumer<Popup>> beforePopupShownListeners = new ArrayList<>();

    /**
     * Creates one step in the onboarding walkthrough.
     *
     * @param targetElement Not null if the step's Popup should highlight some element on the screen, or null if the step should only display a generic dialog.
     */
    public OnboardingStep(Component targetElement) {
        this.targetElement = targetElement;
    }

    public Component getTargetElement() {
        return targetElement;
    }

    public void setTargetElement(Component targetElement) {
        this.targetElement = targetElement;
    }

    public PopupPosition getPosition() {
        return position;
    }

    public void setPosition(PopupPosition position) {
        this.position = position;
    }

    public Component getContent() {
        return content;
    }

    public void setContent(Component content) {
        this.content = content;
    }

    public void setContent(String content) {
        final Div div = new Div();
        div.setText(content);
        div.getElement().getStyle().set("padding", "0 1rem 1rem 1rem");
        div.getElement().getStyle().set("min-width", "15rem");
        this.content = div;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public PopupAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(PopupAlignment alignment) {
        this.alignment = alignment;
    }

    public void addBeforePopupShownListener(SerializableConsumer<Popup> listener) {
        beforePopupShownListeners.add(listener);
    }

    public void removeBeforePopupShownListener(SerializableConsumer<Popup> listener) {
        beforePopupShownListeners.remove(listener);
    }

    void fireBeforePopupShown(Popup popup) {
        beforePopupShownListeners.forEach(it -> it.accept(popup));
    }
}
