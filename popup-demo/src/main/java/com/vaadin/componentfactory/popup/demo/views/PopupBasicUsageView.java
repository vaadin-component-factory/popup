package com.vaadin.componentfactory.popup.demo.views;

import com.vaadin.componentfactory.Popup;
import com.vaadin.componentfactory.PopupAlignment;
import com.vaadin.componentfactory.PopupPosition;
import com.vaadin.componentfactory.PopupVariant;
import com.vaadin.componentfactory.popup.demo.MainLayout;
import com.vaadin.componentfactory.popup.demo.content.LongPopupContent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "basic", layout = MainLayout.class)
public class PopupBasicUsageView extends HorizontalLayout {

    public static final String TEST_TARGET_ELEMENT_ID = "test-target";
    private final Popup popup = new Popup();

    public PopupBasicUsageView() {
        super();

        initPopupContent();

        add(createLeftPane());
        addAndExpand(createExamplePane());
    }

    private Component createLeftPane() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setMaxWidth("25rem");

        layout.add(createOptionsPane());
        layout.add(createActionsPane());

        return layout;
    }

    private Component createActionsPane() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);

        final H4 popupActions = new H4("Popup actions");
        popupActions.getStyle().set("margin-top", "0");
        layout.add(popupActions);

        layout.add(createOpenProgrammaticallyAction());
        layout.add(createBindUnbindAction());

        return layout;
    }

    private Component createBindUnbindAction() {
        return new Button("Unbind from target element", event -> {
            if (popup.getFor() != null) {
                popup.setFor(null);
                popup.setTarget(null);
                event.getSource().setText("Bind to target element by Id");
            } else {
                popup.setFor(TEST_TARGET_ELEMENT_ID);
                event.getSource().setText("Unbind from target element");
            }
        });
    }

    private Component createOpenProgrammaticallyAction() {
        Button action = new Button("Open programmatically", event -> {
            if (popup.isOpened()) {
                popup.hide();
            } else {
                popup.show();
            }
        });
        popup.addPopupOpenChangedEventListener(event -> {
            if (popup.isOpened()) {
                action.setText("Close programmatically");
            } else {
                action.setText("Open programmatically");
            }
        });
        return action;
    }

    private void initPopupContent() {
        popup.add(new LongPopupContent());
    }

    private Component createExamplePane() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.getStyle().set("padding-left", "12rem");
        layout.getStyle().set("padding-top", "12rem");
        layout.setHeight("100rem");

        H4 testTarget = new H4("Click this text to open the Popup!");
        testTarget.setId(TEST_TARGET_ELEMENT_ID);
        layout.add(testTarget);
        popup.setFor(TEST_TARGET_ELEMENT_ID);
        layout.add(popup);

        return layout;
    }

    private Component createOptionsPane() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);

        final H4 popupConfiguration = new H4("Popup configuration");
        popupConfiguration.getStyle().set("margin-top", "0");
        layout.add(popupConfiguration);

        layout.add(createCloseOnClickOption());
        layout.add(createModelessOption());
        layout.add(createCloseOnScrollOption());
        layout.add(createIgnoreTargetClickOption());
        layout.add(createFocusTrapOption());
        layout.add(createRestoreFocusOnCloseOption());
        layout.add(createPointerArrowThemeOption());
        layout.add(createHighlightTargetOption());
        layout.add(createPopupAlignmentOption());
        layout.add(createScrollTargetIntoViewOption());
        layout.add(createPopupPositionOption());

        return layout;
    }

    private Component createRestoreFocusOnCloseOption() {
        popup.setRestoreFocusOnClose(true);
        Checkbox option = new Checkbox("Restore focus on close");
        option.addValueChangeListener(event -> popup.setRestoreFocusOnClose(event.getValue()));
        option.setValue(true);
        return option;
    }

    private Component createScrollTargetIntoViewOption() {
        popup.setScrollTargetIntoView(false);
        Checkbox option = new Checkbox("Scroll target into view on popup open");
        option.addValueChangeListener(event -> popup.setScrollTargetIntoView(event.getValue()));
        option.setValue(false);
        return option;
    }

    private Component createFocusTrapOption() {
        popup.setFocusTrap(false);
        Checkbox option = new Checkbox("Focus trap");
        option.addValueChangeListener(event -> popup.setFocusTrap(event.getValue()));
        option.setValue(false);
        return option;
    }

    private Component createHighlightTargetOption() {
        Checkbox option = new Checkbox("Highlight target element");
        option.addValueChangeListener(event -> popup.setHighlightTarget(event.getValue()));
        return option;
    }

    private Component createIgnoreTargetClickOption() {
        popup.setIgnoreTargetClick(false);
        Checkbox option = new Checkbox("Open on target click");
        option.addValueChangeListener(event -> popup.setIgnoreTargetClick(!event.getValue()));
        option.setValue(true);
        return option;
    }

    private Component createCloseOnScrollOption() {
        popup.setCloseOnScroll(false);
        Checkbox option = new Checkbox("Close Popup when backround scrolls (uncheck Modal first!)");
        option.addValueChangeListener(event -> popup.setCloseOnScroll(event.getValue()));
        return option;
    }

    private Component createModelessOption() {
        popup.setModeless(false);
        Checkbox option = new Checkbox("Popup is modal");
        option.setValue(true);
        option.addValueChangeListener(event -> popup.setModeless(!event.getValue()));
        return option;
    }

    private Component createPopupAlignmentOption() {
        Checkbox alignCenter = new Checkbox();
        alignCenter.setLabel("Align the Popup to the center of target element");
        alignCenter.addValueChangeListener(event -> {
            if (event.getValue()) {
                popup.setAlignment(PopupAlignment.CENTER);
            } else {
                popup.setAlignment(null);
            }
        });
        return alignCenter;
    }

    private Component createPopupPositionOption() {
        RadioButtonGroup<PopupPosition> popupPosition = new RadioButtonGroup<>();
        popupPosition.setLabel("Popup position");
        popupPosition.setItems(PopupPosition.values());
        popupPosition.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        popupPosition.setItemLabelGenerator(item -> {
            switch (item) {
                case BOTTOM:
                    return "To the bottom of the target element";
                case END:
                    return "To the end of the target element";
                default:
                    return "Unknown";
            }
        });
        popupPosition.setValue(PopupPosition.BOTTOM);
        popupPosition.addValueChangeListener(event -> popup.setPosition(event.getValue()));
        return popupPosition;
    }

    private Component createPointerArrowThemeOption() {
        Checkbox option = new Checkbox("Show pointer arrow towards target element");
        option.addValueChangeListener(event -> {
            if (event.getValue()) {
                popup.addThemeVariants(PopupVariant.LUMO_POINTER_ARROW);
            } else {
                popup.removeThemeVariants(PopupVariant.LUMO_POINTER_ARROW);
            }
        });
        return option;
    }

    private Component createCloseOnClickOption() {
        popup.setCloseOnClick(false);
        Checkbox option = new Checkbox("Close Popup when it's clicked");
        option.addValueChangeListener(event -> popup.setCloseOnClick(event.getValue()));
        return option;
    }

}