/*
 * Vaadin Popup for Vaadin 14
 *
 * Copyright 2000-2021 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.componentfactory;

import java.io.Serializable;
import java.util.Objects;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.shared.HasThemeVariant;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;

/**
 * Server-side component for the <code>vcf-popup</code> element.
 *
 * @author Vaadin Ltd
 */
@Tag("vcf-popup")
@NpmPackage(value = "@vaadin-component-factory/vcf-popup", version = "23.3.3")
@JsModule("./flow-component-renderer.js")
@JsModule("@vaadin-component-factory/vcf-popup/src/vcf-popup.js")
public class Popup extends PolymerTemplate<Popup.PopupModel> implements HasThemeVariant<PopupVariant> {
    private Element template;
    private Element container;

    private Popup.PopupHeader popupHeader;
    private Popup.PopupFooter popupFooter;

    public Popup() {
        template = new Element("template");

        container = new Element("div");
        getElement().appendVirtualChild(container);

        // Workaround for: https://github.com/vaadin/flow/issues/3496
        setOpened(false);
    }


    /**
     * Adds a listener for {@code PopupOpenChangedEvent} events fired by the
     * webcomponent.
     *
     * @param listener the listener
     * @return a {@link Registration} for removing the event listener
     */
    public Registration addPopupOpenChangedEventListener(
            ComponentEventListener<PopupOpenChangedEvent> listener) {
        return addListener(PopupOpenChangedEvent.class, listener);
    }

    /**
     * Showing popup, if not showed yet.
     */
    public void show() {
        getElement().callJsFunction("show");
    }

    /**
     * Hiding popup, if it's open.
     */
    public void hide() {
        getElement().callJsFunction("hide");
    }

    /**
     * Opens popup. If popup is not attached yet will open it after attaching *
     *
     * @param opened true to open the popup
     */
    public void setOpened(boolean opened) {
        getModel().setOpened(opened);
        if (template.getProperty("innerHTML", false)) {
            if (opened) {
                show();
            } else {
                hide();
            }
        }
    }

    /**
     * Gets the open state from the popup.
     *
     * @return the {@code opened} property from the popup
     */
    public boolean isOpened() {
        return getModel().isOpened();
    }

    /**
     * Sets the target component for this popup by using the element {@code id}.
     * <p>
     * The element should be in the DOM by the time when the attribute is set, otherwise a warning
     * in the Javascript console is shown.
     *
     * @param id the if of component for this popup, can be {@code null} to
     *           remove the target
     */
    public void setFor(String id) {
        getModel().setFor(id);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        getElement().appendChild(template);
        // Attach <flow-component-renderer>
        getElement().getNode()
                .runWhenAttached(ui -> ui.beforeClientResponse(this,
                        context -> attachComponentRenderer()));

        String id = getModel().getFor();
        if (template.getProperty("innerHTML", false)) {
            if (id == null) {
                getElement().callJsFunction("disconnectedCallback");
            } else {
                getElement().callJsFunction("connectedCallback");
            }
        }
    }

    /**
     * Gets the id of target component of this popup, or {@code null} if it
     * doesn't have a target.
     *
     * @return the id of target component of this popup
     * @see #setFor(String)
     */
    public String getFor() {
        return getModel().getFor();
    }

    /**
     * Use this method to set the element which should be used as a Popup trigger.
     * The target must be placed in the same shadow scope as the Popup element.
     * <p>
     * Note: you can get the Element of a component using {@code Component.getElement()} method.
     *
     * @param element Not-null to set the target element, use null to unset the target
     */
    public void setTarget(Element element) {
        getElement().executeJs("this.target = $0", element);
    }

    /**
     * Sets the preferred position of this popup in relation to the target component set by {@link Popup#setFor(String)}.
     * See {@link PopupPosition} for detailed description of positioning options.
     */
    public void setPosition(PopupPosition position) {
        getModel().setPosition(position.getPropertyValue());
    }

    /**
     * Gets the preferred position of his popup in relation to the target element.
     * Note that the actual position can be different, depending on the space available on the screen.
     * See {@link PopupPosition} for detailed description of positioning options.
     */
    public PopupPosition getPosition() {
        return PopupPosition.fromPropertyValue(getModel().getPosition());
    }


    /**
     * Sets the alignment of this popup in relation to the target component set by {@link Popup#setFor(String)}.
     * See {@link PopupAlignment} for detailed description of alignment options.
     */
    public void setAlignment(PopupAlignment alignment) {
        if (alignment != null) {
            getModel().setAlignment(alignment.getPropertyValue());
        } else {
            getModel().setAlignment(null);
        }
    }

    /**
     * Gets the alignment of his popup in relation to the target element.
     * Note that the actual alignment and position of the popup can be different, depending on the space available on the screen.
     * See {@link PopupAlignment} for detailed description of positioning options.
     */
    public PopupAlignment getAlignment() {
        return PopupAlignment.fromPropertyValue(getModel().getAlignment());
    }

    /**
     * Sets parameter closeOnClick. Default if false. If set to true then popup
     * will be closed when clicking on it and on clicking outside popup. If set
     * to false then popup will be closed when clicking outside popup
     * <p>
     * Should be set before binding to dom. setting after binding will make no
     * effect
     *
     * @param close true to close the popup automatically
     */
    public void setCloseOnClick(boolean close) {
        getModel().setCloseOnClick(close);
    }

    /**
     * gets closeOnClick parameter from popup
     *
     * @return closeOnClick parameter from popup
     */
    public boolean isCloseOnClick() {
        return getModel().isCloseOnClick();
    }

    /**
     * Sets attribute closeOnScroll. Default if false. If set to true then popup
     * will be closed when content outside of the popup is scrolled.
     * <p>
     * Note: The popup has to be modeless in order for closeOnScroll to have any effect. See {@link Popup#setModeless(boolean)}
     *
     * @param close true to close the popup automatically on scroll
     */
    public void setCloseOnScroll(boolean close) {
        getModel().setCloseOnScroll(close);
    }

    /**
     * gets closeOnScroll parameter from popup
     *
     * @return closeOnScroll parameter from popup
     */
    public boolean isCloseOnScroll() {
        return getModel().isCloseOnScroll();
    }

    /**
     * Sets whether popup will open modal or modeless.
     * <p>
     * A modeless popup allows user to interact with the interface under it and won't be closed by pressing the ESC key.
     *
     * @param modeless {@code true} to make the popup modeless, {@code false} to display the popup modal.
     */
    public void setModeless(boolean modeless) {
        getModel().setModeless(modeless);
    }

    /**
     * Gets whether component is set as modal or modeless popup.
     *
     * @return {@code false} if modal popup (default), {@code true} otherwise.
     */
    public boolean isModeless() {
        return getModel().isModeless();
    }

    /**
     * When true the Popup overlay will receive focus when opened and
     * the Tab and Shift+Tab keys will cycle through the Popup's
     * tabbable elements but will not leave the Popup.
     *
     * @param focusTrap {@code true} to enable focus trap
     */
    public void setFocusTrap(boolean focusTrap) {
        getModel().setFocusTrap(focusTrap);
    }

    /**
     * Gets whether the popup will receive a focus and the focus will be trapped in the popup
     * (see {@link Popup#setFocusTrap(boolean)})
     *
     * @return {@code false} if focus trap is not set (default), {@code true} otherwise.
     */
    public boolean isFocusTrap() {
        return getModel().isFocusTrap();
    }

    /**
     * When true, the popup target element will be highlighted, to make it absolutely clear what element the popup refers to.
     * Handy for example when implementing walkthrough scenario (also known as onboarding).
     *
     * @param highlightTarget {@code true} to highlight target element on the screen
     */
    public void setHighlightTarget(boolean highlightTarget) {
        getModel().setHighlightTarget(highlightTarget);
    }

    /**
     * Gets whether the target element will be highlighted when the popup is opened.
     *
     * @return {@code true} when the target element will be highlighted
     */
    public boolean isHighlightTarget() {
        return getModel().isHighlightTarget();
    }

    /**
     * When set to false (default), the Popup will be shown when the target element (set either by 'for' or 'target' property)
     * is clicked. When set to true, you have to open the Popup manually by calling the 'show()' method on the Popup.
     * <p>
     * By default, it's set to 'false' for backwards compatibility.
     *
     * @param ignoreTargetClick Whether to ignore the target clicking.
     */
    public void setIgnoreTargetClick(boolean ignoreTargetClick) {
        getModel().setIgnoreTargetClick(ignoreTargetClick);
    }

    public boolean isIgnoreTargetClick() {
        return getModel().isIgnoreTargetClick();
    }

    /**
     * Adds the given components into this popup.
     * <p>
     * The elements in the DOM will not be children of the {@code <vcf-popup>}
     * element, but will be inserted into an overlay that is attached into the
     * {@code <body>}.
     *
     * @param components the components to add
     */
    public void add(Component... components) {
        Objects.requireNonNull(components, "Components should not be null");
        for (Component component : components) {
            Objects.requireNonNull(component,
                    "Component to add cannot be null");
            container.appendChild(component.getElement());
        }
    }

    /**
     * Removes components from popup. Components should be in popup, otherwise
     * IllegalArgumentException will be raised
     *
     * @param components the components to remove
     */
    public void remove(Component... components) {
        Objects.requireNonNull(components, "Components should not be null");
        for (Component component : components) {
            Objects.requireNonNull(component,
                    "Component to remove cannot be null");
            if (container.equals(component.getElement().getParent())) {
                container.removeChild(component.getElement());
            } else {
                throw new IllegalArgumentException("The given component ("
                        + component + ") is not a child of this component");
            }
        }
    }

    /**
     * Removes all components from popup
     */
    public void removeAll() {
        container.removeAllChildren();
    }

    /**
     * Adds the given component into this popup at the given index.
     * <p>
     * The element in the DOM will not be child of the {@code <vcf-popup>}
     * element, but will be inserted into an overlay that is attached into the
     * {@code <body>}.
     *
     * @param index     the index, where the component will be added.
     * @param component the component to add
     */
    public void addComponentAtIndex(int index, Component component) {
        Objects.requireNonNull(component, "Component should not be null");
        if (index < 0) {
            throw new IllegalArgumentException(
                    "Cannot add a component with a negative index");
        }
        // The case when the index is bigger than the children count is handled
        // inside the method below
        container.insertChild(index, component.getElement());
    }

    /**
     * Sets the title to be rendered on the popup header.
     *
     * @param title title to be rendered
     */
    public void setHeaderTitle(String title) {
        getElement().setProperty("headerTitle", title);
    }

    /**
     * Gets the title set for the popup header.
     *
     * @return the title or an empty string, if a header title is not defined.
     */
    public String getHeaderTitle() {
        return getElement().getProperty("headerTitle", "");
    }

    /**
     * Gets the object from which components can be added or removed from the
     * popup header area. The header is displayed only if there's a
     * {@link #getHeaderTitle()} or at least one component added with
     * {@link Popup.PopupHeaderFooter#add(Component...)}.
     *
     * @return the header object
     */
    public Popup.PopupHeader getHeader() {
        if (this.popupHeader == null) {
            this.popupHeader = new Popup.PopupHeader(this);
        }
        return this.popupHeader;
    }

    /**
     * Set the {@code aria-label} attribute for assistive technologies like
     * screen readers. An {@code undefined} value for this property (the
     * default) means that the {@code aria-label} attribute is not present at
     * all.
     * <p>
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     *
     * @return the {@code ariaLabel} property from the webcomponent
     */
    public String getAriaLabel() {
        return getElement().getProperty("ariaLabel");
    }

    /**
     * Set the {@code aria-label} attribute for assistive technologies like
     * screen readers. An {@code undefined} value for this property (the
     * default) means that the {@code aria-label} attribute is not present at
     * all.
     *
     * @param ariaLabel
     *            the String value to set
     */
    public void setAriaLabel(String ariaLabel) {
        getElement().setProperty("ariaLabel",
                ariaLabel == null ? "" : ariaLabel);
    }

    /**
     * Gets the object from which components can be added or removed from the
     * popup footer area. The footer is displayed only if there's at least one
     * component added with {@link Popup.PopupHeaderFooter#add(Component...)}.
     *
     * @return the header object
     */
    public Popup.PopupFooter getFooter() {
        if (this.popupFooter == null) {
            this.popupFooter = new Popup.PopupFooter(this);
        }
        return this.popupFooter;
    }

    /**
     * Class for adding and removing components to the header part of a popup.
     */
    final public static class PopupHeader extends Popup.PopupHeaderFooter {
        private PopupHeader(Popup popup) {
            super("headerRenderer", popup);
        }
    }

    /**
     * Class for adding and removing components to the footer part of a popup.
     */
    final public static class PopupFooter extends Popup.PopupHeaderFooter {
        private PopupFooter(Popup popup) {
            super("footerRenderer", popup);
        }
    }

    /**
     * This class defines the common behavior for adding/removing components to
     * the header and footer parts. It also creates the root element where the
     * components will be attached to as well as the renderer function used by
     * the popup.
     */
    abstract static class PopupHeaderFooter implements Serializable {
        protected final Element root;
        private final String rendererFunction;
        private final Component popup;
        boolean rendererCreated = false;

        protected PopupHeaderFooter(String rendererFunction,
                                    Component popup) {
            this.rendererFunction = rendererFunction;
            this.popup = popup;
            root = new Element("div");
            root.getStyle().set("display", "contents");
        }

        /**
         * Adds the given components to the container.
         *
         * @param components the components to be added.
         */
        public void add(Component... components) {
            Objects.requireNonNull(components, "Components should not be null");
            for (Component component : components) {
                Objects.requireNonNull(component,
                        "Component to add cannot be null");
                root.appendChild(component.getElement());
            }
            if (!isRendererCreated()) {
                initRenderer();
            }
        }

        /**
         * Removes the given components from the container.
         *
         * <p>
         * Note that the component needs to be removed from this method in order
         * to guarantee the correct state of the component.
         *
         * @param components the components to be removed.
         */
        public void remove(Component... components) {
            Objects.requireNonNull(components, "Components should not be null");
            for (Component component : components) {
                Objects.requireNonNull(component,
                        "Component to remove cannot be null");
                if (root.equals(component.getElement().getParent())) {
                    root.removeChild(component.getElement());
                }
            }
            if (root.getChildCount() == 0) {
                popup.getElement()
                        .executeJs("this." + rendererFunction + " = null;");
                setRendererCreated(false);
            }
        }

        /**
         * Removes all components from the container.
         */
        public void removeAll() {
            root.removeAllChildren();
            popup.getElement()
                    .executeJs("this." + rendererFunction + " = null;");
            setRendererCreated(false);
        }

        /**
         * Method called to create the renderer function using
         * {@link #rendererFunction} as the property name.
         */
        void initRenderer() {
            if (root.getChildCount() == 0) {
                return;
            }
            popup.getElement().appendChild(root);
            popup.getElement().executeJs("this." + rendererFunction
                    + " = (root) => {" + "if (root.firstChild) { "
                    + "   return;" + "}" + "root.appendChild($0);" + "}", root);
            setRendererCreated(true);
        }

        /**
         * Gets whether the renderer function exists or not
         *
         * @return the renderer function state
         */
        boolean isRendererCreated() {
            return rendererCreated;
        }

        /**
         * Sets the renderer function creation state. To avoid making a
         * JavaScript execution to get the information from the client, this is
         * done on the server by setting it to <code>true</code> on
         * {@link #initRenderer()} and to <code>false</code> when the last child
         * is removed in {@link #remove(Component...)} or when an auto attached
         * popup is closed.
         *
         * @param rendererCreated
         */
        void setRendererCreated(boolean rendererCreated) {
            this.rendererCreated = rendererCreated;
        }

        public Element getElement() {
            return root;
        }
    }


    private void attachComponentRenderer() {
        String appId = UI.getCurrent().getInternals().getAppId();
        int nodeId = container.getNode().getId();
        String renderer = String.format(
                "<flow-component-renderer appid=\"%s\" nodeid=\"%s\"></flow-component-renderer>",
                appId, nodeId);
        template.setProperty("innerHTML", renderer);
        if (isOpened()) {
            show();
        }
    }

    /**
     * This model binds properties between java(Popup) and
     * polymer(vcf-popup.html)
     */
    public interface PopupModel extends TemplateModel {
        void setOpened(boolean opened);

        boolean isOpened();

        void setFor(String id);

        String getFor();

        void setCloseOnClick(boolean close);

        boolean isCloseOnClick();

        void setCloseOnScroll(boolean close);

        boolean isCloseOnScroll();

        void setModeless(boolean modeless);

        boolean isModeless();

        void setPosition(String position);

        String getPosition();

        void setAlignment(String alignment);

        String getAlignment();

        void setFocusTrap(boolean focusTrap);

        boolean isFocusTrap();

        void setHighlightTarget(boolean highlightTarget);

        boolean isHighlightTarget();

        void setIgnoreTargetClick(boolean ignoreTargetClick);

        boolean isIgnoreTargetClick();

    }

    @DomEvent("popup-open-changed")
    public static class PopupOpenChangedEvent extends ComponentEvent<Popup> {

        private boolean opened;

        public PopupOpenChangedEvent(Popup source,
                                     boolean fromClient,
                                     @EventData("event.detail.opened") boolean opened) {
            super(source, fromClient);
            this.opened = opened;
        }

        public boolean isOpened() {
            return opened;
        }
    }

}