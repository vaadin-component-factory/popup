package com.vaadin.componentfactory.popup.demo.views;

import com.vaadin.componentfactory.Popup;
import com.vaadin.componentfactory.popup.demo.MainLayout;
import com.vaadin.componentfactory.popup.demo.content.LongPopupContent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;

@Route(value = "select", layout = MainLayout.class)
@CssImport(value = "./styles/custom-popup-style.css", themeFor = "vcf-popup-overlay")
public class SelectView extends VerticalLayout {

    public SelectView() {
        add(new H3("Select in a popup"));

        addViewDescription();

        VerticalLayout buttonLayout = new VerticalLayout();
        buttonLayout.setSpacing(false);
        buttonLayout.setPadding(false);
        addSimpleTextInHeaderExample(buttonLayout);
        add(buttonLayout);
    }

    private void addSimpleTextInHeaderExample(VerticalLayout layout) {
        Button button = new Button("Show popup with simple textual header");
        Popup popup = createDefaultPopup(button, layout);
        popup.setHeaderTitle("This is title");
        layout.add(button);
    }

    private void addViewDescription() {
        add(new Paragraph("This demo showcases a possibility to use a Select in the popup"));
    }

    private Popup createDefaultPopup(Component forComponent, VerticalLayout layout) {
        Popup popup = new Popup();
        Select<String> select = new Select<>();
        select.setItems("Item 1", "Item 2", "Item 3");
        popup.add(select);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems("Item 1", "Item 2", "Item 3");
        popup.add(comboBox);
        popup.setTarget(forComponent.getElement());
        layout.add(popup);
        return popup;
    }
}