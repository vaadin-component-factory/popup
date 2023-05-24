/*
 * Copyright 2000-2022 Vaadin Ltd.
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

import java.util.concurrent.atomic.AtomicLong;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;

/**
 * A renderer that renders a component provided by {@code componentFunction}.
 * When this component is clicked, the Popup is generated using provided
 * {@link PopupGenerator}, attached to the DOM and shown.
 * The popup is removed from the DOM tree once it's hidden.
 *
 * @param <ITEM> the type of the input object that can be used by the rendered
 *               component
 * @author Vaadin Ltd
 */
public class ComponentWithPopupRenderer<ITEM> extends ComponentRenderer<Component, ITEM> {

    protected PopupGenerator<ITEM> itemPopupGenerator;

    protected SerializableFunction<ITEM, ? extends Component> componentFunction;

    private static final AtomicLong idCounter = new AtomicLong(0);


    /**
     * Creates a new renderer instance using the provided
     * {@code componentFunction} and {@code itemPopupGenerator}.
     *
     * @param itemPopupGenerator should return Popup instance based on the provided grid item
     * @param componentFunction  function which returns the component that
     *                           will be rendered in the grid. A popup will appear
     *                           when this component is clicked.
     */
    public ComponentWithPopupRenderer(SerializableFunction<ITEM, ? extends Component> componentFunction,
                                      PopupGenerator<ITEM> itemPopupGenerator) {
        this.itemPopupGenerator = itemPopupGenerator;
        this.componentFunction = componentFunction;
    }

    @Override
    public Component createComponent(ITEM item) {
        HasComponents container = createWrappingContainer();

        final Component target = createTargetComponent(item);
        target.getElement().addEventListener("click",
                clickEvent -> generateAndShowPopup(item, container, target));
        container.add(target);

        return (Component) container;
    }

    protected Popup createPopup(ITEM item, Component target) {
        Popup popup = itemPopupGenerator.apply(item);
        popup.setFor(target.getId().orElse(null));
        return popup;
    }

    protected Component createTargetComponent(ITEM item) {
        final Component component = componentFunction.apply(item);
        component.setId(createUniqueId());
        return component;
    }

    protected HasComponents createWrappingContainer() {
        return new Div();
    }

    protected String createUniqueId() {
        return "item-with-popup-" + idCounter.incrementAndGet();
    }

    protected void generateAndShowPopup(ITEM item, HasComponents container, Component target) {
        Popup popup = createPopup(item, target);
        popup.addPopupOpenChangedEventListener(event -> {
            // remove the popup from the DOM tree when it's closed
            if (!event.isOpened()) {
                container.remove(popup);
            }
        });
        container.add(popup);
        popup.show();
    }
}
