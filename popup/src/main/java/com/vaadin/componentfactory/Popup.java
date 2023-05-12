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

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.Objects;

/**
 * Server-side component for the <code>vcf-popup</code> element.
 *
 * @author Vaadin Ltd
 */
@Tag("vcf-popup")
@NpmPackage(value = "@vaadin-component-factory/vcf-popup", version = "23.3.0")
@JsModule("./flow-component-renderer.js")
@JsModule("@vaadin-component-factory/vcf-popup/src/vcf-popup.js")
public class Popup extends PolymerTemplate<Popup.PopupModel> {
    private Element template;
    private Element container;

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
     * @param listener
     *            the listener
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
     * Sets the target component for this popup.
     * <p>
     * By default, the context menu can be opened with a left click or touch on
     * the target component.
     *
     * @param id
     *            the if of component for this popup, can be {@code null} to
     *            remove the target
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
     * Sets parameter closeOnClick. Default if false. If set to true then popup
     * will be closed when clicking on it and on clicking outside popup. If set
     * to false then popup will be closed when clicking outside popup
     *
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
     * Adds the given components into this dialog.
     * <p>
     * The elements in the DOM will not be children of the {@code <vcf-popup>}
     * element, but will be inserted into an overlay that is attached into the
     * {@code <body>}.
     *
     * @param components
     *            the components to add
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
     * @param components
     *            the components to remove
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
     * Adds the given component into this dialog at the given index.
     * <p>
     * The element in the DOM will not be child of the {@code <vcf-popup>}
     * element, but will be inserted into an overlay that is attached into the
     * {@code <body>}.
     *
     * @param index
     *            the index, where the component will be added.
     *
     * @param component
     *            the component to add
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