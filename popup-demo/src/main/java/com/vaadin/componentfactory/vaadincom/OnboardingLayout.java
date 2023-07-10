package com.vaadin.componentfactory.vaadincom;


import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;

import java.util.Objects;

public class OnboardingLayout extends VerticalLayout implements RouterLayout {

    private Div container = new Div();

    public OnboardingLayout() {
        setWidth("100%");

        Div topBar = new Div();
        topBar.setText("Top bar");
        topBar.setId("top-bar");
        topBar.setWidth("100%");
        topBar.setHeight("40px");
        topBar.getStyle().set("background-color", "#EEE");
        add(topBar);

        Div leftBar = new Div();
        leftBar.setText("Left bar");
        leftBar.setId("left-bar");
        leftBar.setHeight("300px");
        leftBar.getStyle().set("background-color", "#EEE");
        leftBar.getStyle().set("min-width", "100px");

        final HorizontalLayout horizontalLayout = new HorizontalLayout(leftBar, container);
        horizontalLayout.setWidthFull();
        add(horizontalLayout);
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        if (content != null) {
            container.getElement()
                    .appendChild(Objects.requireNonNull(content.getElement()));
        }
    }
}
