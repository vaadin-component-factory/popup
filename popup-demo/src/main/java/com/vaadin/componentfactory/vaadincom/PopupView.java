package com.vaadin.componentfactory.vaadincom;

import java.util.concurrent.ThreadLocalRandom;

import com.vaadin.componentfactory.Popup;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.demo.DemoView;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;

@Route("popup")
@CssImport(value = "./styles/custom-popup-style.css", themeFor = "vcf-popup-overlay")
public class PopupView extends DemoView {

    @Override
    protected void initView() {
        addBasicExample();
        addCloseOnClickExample();
        addOpenedExample();
        addShowHideExample();
        addUnbindExample();
        addHeaderAndFooterExample();
        addStyledHeaderAndFooterExample();
        addCloseOnScrollExample();
        addModelessExample();
    }

    private void addCloseOnScrollExample() {
        Button button = new Button("Push Me");
        button.setId("push-me-scroll");

        Popup popup = new Popup();
        popup.setFor(button.getId().orElse(null));
        popup.setCloseOnScroll(true);
        popup.setModeless(true);
        VerticalLayout content = new VerticalLayout();
        content.add(new Span("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Nullam at arcu a est sollicitudin euismod. Nunc tincidunt ante vitae massa. Et harum quidem rerum facilis est et expedita distinctio. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus"));
        content.setMaxWidth("400px");
        popup.add(content);

        Div closeOnScrollStatus = new Div();
        closeOnScrollStatus.setText("Close on scroll: " + popup.isCloseOnScroll());
        Button toggleCloseOnScroll = new Button("Toggle close on scroll");
        toggleCloseOnScroll.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        toggleCloseOnScroll.addClickListener(buttonClickEvent -> {
            popup.setCloseOnScroll(!popup.isCloseOnScroll());
            closeOnScrollStatus.setText("Close on scroll: " + popup.isCloseOnScroll());
        });

        addCard("Popup with close on scroll", button, popup,
                closeOnScrollStatus, toggleCloseOnScroll);
    }

    private void addStyledHeaderAndFooterExample() {
        Button button = new Button("Push Me");
        button.setId("push-me-header-and-footer");

        VerticalLayout content = new VerticalLayout();
        content.add(new Span("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Nullam at arcu a est sollicitudin euismod. Nunc tincidunt ante vitae massa. Et harum quidem rerum facilis est et expedita distinctio. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus"));
        content.setMaxWidth("400px");

        Popup popup = new Popup();
        popup.setFor(button.getId().orElse(null));
        popup.add(content);
        popup.setHeaderTitle("This is title");

        Button cancel = new Button("Cancel");
        Button apply = new Button("Apply");
        apply.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        popup.getFooter().add(cancel, apply);

        popup.setThemeName("custom-popup-style");

        addCard("Popup with styled header and footer", button, popup);
    }

    private void addHeaderAndFooterExample() {
        Button button = new Button("Push Me");
        button.setId("push-me-header-and-footer");

        VerticalLayout content = new VerticalLayout();
        content.add(new Span("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Nullam at arcu a est sollicitudin euismod. Nunc tincidunt ante vitae massa. Et harum quidem rerum facilis est et expedita distinctio. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec ipsum massa, ullamcorper in, auctor et, scelerisque sed, est. Duis viverra diam non justo. Nulla est"));
        content.setMaxWidth("300px");
        content.setMaxHeight("200px");

        Popup popup = new Popup();
        popup.setFor(button.getId().orElse(null));
        popup.add(content);
        popup.setHeaderTitle("This is title");

        Button closeBtn = new Button(LumoIcon.CROSS.create());
        closeBtn.addClickListener(e -> popup.hide());
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        popup.getHeader().add(closeBtn);

        Button cancel = new Button("Cancel");
        Button apply = new Button("Apply");
        apply.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        popup.getFooter().add(cancel, apply);

        addCard("Popup with header and footer", button, popup);
    }

    private void addBasicExample() {
        Button button = new Button("Push Me");
        button.setId("push-me");

        Popup popup = new Popup();
        popup.setFor(button.getId().orElse(null));
        Div text = new Div();
        text.setText("element 1");
        Div text2 = new Div();
        text2.setText("element 2");
        popup.add(text, text2);

        Div closeOnClickStatus = new Div();
        closeOnClickStatus.setText("Close on click: " + popup.isCloseOnClick());

        Div eventStatus = new Div();
        popup.addPopupOpenChangedEventListener(event -> {
        	if (event.isOpened())
        		eventStatus.setText("Popup opened");
        	else
        		eventStatus.setText("Popup closed");
        });
        
        addCard("Basic popup usage", button, popup, closeOnClickStatus, eventStatus);
    }

    private void addModelessExample() {
        Button button = new Button("Push Me");
        button.setId("push-me-modeless");

        Popup popup = new Popup();
        popup.setModeless(true);
        popup.setFor(button.getId().orElse(null));
        VerticalLayout content = new VerticalLayout();
        content.add(new Span("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Nullam at arcu a est sollicitudin euismod. Nunc tincidunt ante vitae massa. Et harum quidem rerum facilis est et expedita distinctio. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus"));
        content.setMaxWidth("400px");
        popup.add(content);

        Div modelessStatus = new Div();
        modelessStatus.setText("Is modeless " + popup.isModeless());

        addCard("Modeless example", button, popup, modelessStatus);
    }

    private void addCloseOnClickExample() {
        Button button = new Button("Push Me");
        button.setId("push-me");

        Popup popup = new Popup();
        popup.setFor(button.getId().orElse(null));
        popup.setCloseOnClick(true);
        Div text = new Div();
        text.setText("element 1");
        Div text2 = new Div();
        text2.setText("element 2");
        popup.add(text, text2);

        Div closeOnClickStatus = new Div();
        closeOnClickStatus.setText("Close on click: " + popup.isCloseOnClick());

        addCard("Popup with close on popup usage", button, popup,
                closeOnClickStatus);
    }

    private void addOpenedExample() {
        Div target = new Div();
        target.setText(
                "I have popup, click me. P.S: pop-up will gone with first click anywhere");
        target.setId("div-push-me");

        Popup popup = new Popup();
        popup.setOpened(true);
        popup.setFor(target.getId().orElse(null));

        popup.add(new Icon(VaadinIcon.VAADIN_H), new Icon(VaadinIcon.VAADIN_H),
                new Icon(VaadinIcon.VAADIN_H));

        addCard("Opened popup usage", target, popup);
    }

    private void addShowHideExample() {
        Div target = new Div();
        target.setText("I have popup, click me(I dont change popup content)");
        target.setId("div-push-me-2");

        Popup popup = new Popup();
        popup.setFor(target.getId().orElse(null));
        popup.add(new Icon(VaadinIcon.VAADIN_H), new Icon(VaadinIcon.VAADIN_H),
                new Icon(VaadinIcon.VAADIN_H));

        Button button = new Button("Change popup content");
        button.addClickListener(e -> {
            if (popup.isOpened()) {
                popup.hide();
            } else {
                int elCount = ThreadLocalRandom.current().nextInt(1, 10);
                popup.removeAll();
                for (int i = 0; i < elCount; i++) {
                    popup.add(new Icon(VaadinIcon.VAADIN_H));
                }
                popup.show();
            }
        });

        HorizontalLayout hl = new HorizontalLayout();
        hl.add(target, popup, button);

        addCard("Show/Hide popup usage.", hl);
    }

    private void addUnbindExample() {
        Div target = new Div();
        target.setText("I have popup, click me");
        target.setId("div-push-me-2");

        Popup popup = new Popup();
        popup.setFor(target.getId().orElse(null));
        Icon orange = new Icon(VaadinIcon.VAADIN_H);
        orange.setColor("orange");
        popup.add(new Icon(VaadinIcon.VAADIN_H), orange,
                new Icon(VaadinIcon.VAADIN_H));

        Button button = new Button("Unbind vaadin");
        button.addClickListener(e -> {
            if (popup.getFor() != null) {
                popup.setFor(null);
                button.setText("Bind vaadin");
            } else {
                popup.setFor(target.getId().orElse(null));
                button.setText("Unbind vaadin");
            }
        });

        HorizontalLayout hl = new HorizontalLayout();
        hl.add(target, popup, button);

        addCard("Bind/Unbind popup to target usage.", hl);
    }
}