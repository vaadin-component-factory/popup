package com.vaadin.componentfactory;

import java.util.Objects;

/*
 * #%L
 * Vaadin Popup for Vaadin 10
 * %%
 * Copyright (C) 2017 - 2018 Vaadin Ltd
 * %%
 * This program is available under Commercial Vaadin Add-On License 3.0
 * (CVALv3).
 * See the file license.html distributed with this software for more
 * information about licensing.
 * You should have received a copy of the CVALv3 along with this program.
 * If not, see <http://vaadin.com/license/cval-3>.
 * #L%
 */

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.templatemodel.TemplateModel;

/**
 * Server-side component for the <code>vcf-popup</code> element.
 *
 * @author Vaadin Ltd
 */
@Tag("vcf-popup")

@HtmlImport("flow-component-renderer.html")
@HtmlImport("frontend://bower_components/vcf-popup/src/vcf-popup.html")

@NpmPackage(value = "@vaadin-component-factory/vcf-popup", version = "1.2.0")
@JsModule("./flow-component-renderer.js")
@JsModule("@vaadin-component-factory/vcf-popup/src/vcf-popup.js")
public class Popup extends PolymerTemplate<Popup.PopupModel> {
    private Element template;
    private Element container;

    public Popup() {
        template = new Element("template");
        getElement().appendChild(template);

        container = new Element("div");
        getElement().appendVirtualChild(container);

        // Attach <flow-component-renderer>
        getElement().getNode()
                .runWhenAttached(ui -> ui.beforeClientResponse(this,
                        context -> attachComponentRenderer()));

        // Workaround for: https://github.com/vaadin/flow/issues/3496
        setOpened(false);
    }

    /**
     * Showing popup, if not showed yet.
     */
    public void show() {
        getElement().callFunction("show");
    }

    /**
     * Hiding popup, if it's open.
     */
    public void hide() {
        getElement().callFunction("hide");
    }

    /**
     * Opens popup. If popup is not attached yet will open it after attaching *
     *
     * @param opened
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
        if (template.getProperty("innerHTML", false)) {
            if (id == null) {
                getElement().callFunction("disconnectedCallback");
            } else {
                getElement().callFunction("connectedCallback");
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
     * @param close
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
}
