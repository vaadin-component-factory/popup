package com.vaadin.componentfactory.popup.demo.views;

import com.vaadin.componentfactory.Popup;
import com.vaadin.componentfactory.popup.demo.MainLayout;
import com.vaadin.componentfactory.popup.demo.content.LongPopupContent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;

@Route(value = "header-footer", layout = MainLayout.class)
@CssImport(value = "./styles/custom-popup-style.css", themeFor = "vcf-popup-overlay")
public class HeaderAndFooterView extends VerticalLayout {

    public HeaderAndFooterView() {
        add(new H3("Add header and footer to Popup"));

        addViewDescription();

        VerticalLayout buttonLayout = new VerticalLayout();
        buttonLayout.setSpacing(false);
        buttonLayout.setPadding(false);
        addSimpleTextInHeaderExample(buttonLayout);
        addTextAndCloseButtonInHeaderExample(buttonLayout);
        addHeaderAndFooterExample(buttonLayout);
        addStyledHeaderAndFooterExample(buttonLayout);
        add(buttonLayout);
    }

    private void addSimpleTextInHeaderExample(VerticalLayout layout) {
        Button button = new Button("Show popup with simple textual header");
        Popup popup = createDefaultPopup(button, layout);
        popup.setHeaderTitle("This is title");
        layout.add(button);
    }

    private void addTextAndCloseButtonInHeaderExample(VerticalLayout layout) {
        Button button = new Button("Show popup with textual header and close button");
        Popup popup = createDefaultPopup(button, layout);
        popup.setHeaderTitle("This is title");
        addCloseButtonToHeader(popup);
        layout.add(button);
    }

    private void addHeaderAndFooterExample(VerticalLayout layout) {
        Button button = new Button("Header and footer example");
        Popup popup = createDefaultPopup(button, layout);
        popup.setHeaderTitle("This is title");
        addCloseButtonToHeader(popup);
        addOkAndCancelToFooter(popup);
        layout.add(button);
    }

    private void addStyledHeaderAndFooterExample(VerticalLayout layout) {
        Button button = new Button("Header and footer with custom styling");
        Popup popup = createDefaultPopup(button, layout);
        popup.setThemeName("custom-popup-style");
        popup.setHeaderTitle("This is title");
        addOkAndCancelToFooter(popup);
        layout.add(button);
    }

    private void addOkAndCancelToFooter(Popup popup) {
        Button cancel = new Button("Cancel");
        Button apply = new Button("Ok");
        apply.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        popup.getFooter().add(cancel, apply);
    }

    private void addCloseButtonToHeader(Popup popup) {
        Button closeBtn = new Button(LumoIcon.CROSS.create());
        closeBtn.addClickListener(e -> popup.hide());
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        popup.getHeader().add(closeBtn);
    }

    private void addViewDescription() {
        add(new Paragraph("This demo showcases a possibility to easily define a header and a footer for the Popup. " +
                "You can use the `setHeaderTitle` method to quickly define the header title text. If you need more complicated header content " +
                "you can use the `getHeader` method to get the header component and add your own content to it using `getHeader.add(Component)`. " +
                "The same applies to the footer - add components (for example buttons) to the footer using `getFooter.add(Component)`."));
    }

    private Popup createDefaultPopup(Component forComponent, VerticalLayout layout) {
        Popup popup = new Popup();
        popup.add(new LongPopupContent());
        popup.setTarget(forComponent.getElement());
        layout.add(popup);
        return popup;
    }
}