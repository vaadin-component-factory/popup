package com.vaadin.componentfactory.popup.demo.content;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LongPopupContent extends VerticalLayout {

    public LongPopupContent() {
        add(new Span("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Nullam at arcu a est sollicitudin euismod. Nunc tincidunt ante vitae massa. Et harum quidem rerum facilis est et expedita distinctio. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat."));
        add(new HorizontalLayout(new Button("Action 1"), new Button("Action 2")));
        add(new Span("Lorem ipsum dolor sit amet. Donec ipsum massa, ullamcorper in, auctor et, scelerisque sed, est. Duis viverra diam non justo. Nulla est. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus."));
        setMaxWidth("25rem");
        setMaxHeight("20rem");
    }
}
