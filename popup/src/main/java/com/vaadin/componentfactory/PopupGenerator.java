/*
 * Copyright 2000-2023 Vaadin Ltd.
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

import com.vaadin.flow.function.SerializableFunction;

/**
 * {@link PopupGenerator} can be used to create the popup, which will be shown
 * to the user for an item.
 *
 * @param <T> item type
 * @author Vaadin Ltd
 */
@FunctionalInterface
public interface PopupGenerator<T> extends SerializableFunction<T, Popup> {

    /**
     * Gets a caption for the {@code item}.
     *
     * @param item the item to get caption for
     * @return the caption of the item, not {@code null}
     */
    @Override
    Popup apply(T item);
}
