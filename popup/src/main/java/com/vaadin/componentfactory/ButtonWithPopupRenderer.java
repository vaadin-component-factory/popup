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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.function.ValueProvider;

/**
 * A renderer that renders a button with provided label in the grid column.
 * When this button is clicked, the Popup is generated using provided
 * {@link PopupGenerator}, attached to the DOM and shown.
 * The popup is removed from the DOM tree once it's hidden.
 *
 * @param <ITEM> the type of the input object that can be used by the rendered
 *               component
 * @author Vaadin Ltd
 */
public class ButtonWithPopupRenderer<ITEM> extends ComponentWithPopupRenderer<ITEM> {

    protected final ValueProvider<ITEM, String> buttonLabelGenerator;


    /**
     * Creates a new renderer instance using the provided generators.
     *
     * @param itemPopupGenerator   the item popup generator
     * @param buttonLabelGenerator generator of labels of the buttons shown in the grid
     */
    public ButtonWithPopupRenderer(ValueProvider<ITEM, String> buttonLabelGenerator,
                                   PopupGenerator<ITEM> itemPopupGenerator) {
        super(null, itemPopupGenerator);
        this.buttonLabelGenerator = buttonLabelGenerator;
    }

    @Override
    protected Component addComponentToContainer(ITEM item, HasComponents container) {
        Button button = new Button(buttonLabelGenerator.apply(item));
        button.setId(createUniqueId());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        button.addClickListener(clickEvent -> generateAndShowPopup(item, container, button));
        container.add(button);
        return button;
    }

    private void generateAndShowPopup(ITEM item, HasComponents container, Button target) {
        Popup popup = itemPopupGenerator.apply(item);
        popup.setFor(target.getId().orElse(null));
        popup.addPopupOpenChangedEventListener(event -> {
            // remove the popup from the DOM tree when it's closed
            if (!event.isOpened()) {
                container.remove(popup);
            }
        });
        container.add(popup);
        popup.show();
    }

    @Override
    protected void addPopupToContainer(ITEM item, HasComponents container, Component component) {
        // noop - popup is attached to the DOM only when a button is clicked
    }
}
