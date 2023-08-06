package com.vaadin.componentfactory.popup.demo.views;

import com.vaadin.componentfactory.PopupPosition;
import com.vaadin.componentfactory.onboarding.Onboarding;
import com.vaadin.componentfactory.onboarding.OnboardingStep;
import com.vaadin.componentfactory.popup.demo.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "onboarding", layout = MainLayout.class)
public class OnboardingView extends VerticalLayout {

    public OnboardingView() {
        final H3 header = new H3("How to use Popup to create an onboarding experience");
        add(header);

        final Paragraph paragraph = new Paragraph("The idea is to use the Popup component to create a simple" +
                " Onboarding (also known as Walkthrough) experience for the  user. A simple API was created as part of " +
                "the component to make it easy to create such onboarding. There are two classes: Onboarding and OnboardingStep. " +
                "To use it, create an instance of the Onboarding class and fill it with the OnboardingSteps. In the simplest form, " +
                "you only have to provide three things for each OnboardingStep: a target element, a header text, and a content text.");
        add(paragraph);

        final Button actionButton = new Button("Some Action");
        add(actionButton);

        Onboarding onboarding = createOnboarding(header, paragraph, actionButton);
        final Button startOnboarding = new Button("Start onboarding", event -> onboarding.start());
        startOnboarding.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(startOnboarding);
    }

    private Onboarding createOnboarding(H3 header, Paragraph paragraph, Button makePurchase) {
        Onboarding onboarding = new Onboarding();

        onboarding.addStep(createStep1(header));
        onboarding.addStep(createStep2(paragraph));
        onboarding.addStep(createStep3(makePurchase));
        onboarding.addStep(createStep4());

        return onboarding;
    }

    private OnboardingStep createStep4() {
        final OnboardingStep onboardingStep4 = new OnboardingStep(null);
        onboardingStep4.setHeader("No target");
        onboardingStep4.setContent("This onboarding step does not have any related target element");
        return onboardingStep4;
    }

    private OnboardingStep createStep3(Button makePurchase) {
        final OnboardingStep onboardingStep3 = new OnboardingStep(makePurchase);
        onboardingStep3.setHeader("Popup with rich content");

        VerticalLayout content = new VerticalLayout();
        content.setWidthFull();
        content.add(new Paragraph("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc at turpis nec nisl lobortis convallis. Curabitur nec ipsum mauris. Proin vitae consectetur justo, sit amet ultricies ex. Proin posuere sem vel nunc lobortis lacinia. Quisque est felis, gravida in hendrerit sagittis, egestas tempor ante. Etiam vulputate urna lorem, eget rutrum sem auctor eget. Aliquam fringilla ut lacus a consectetur. Ut congue vitae quam nec suscipit. Mauris lacinia libero ex, non porttitor nisi elementum sit amet."));
        final Image vaadinLogo = new Image("https://vaadin.com/images/trademark/PNG/VaadinLogo_RGB_500x155.png", "Vaadin Logo");
        vaadinLogo.setWidth("15rem");
        content.add(vaadinLogo);
        content.add(new Paragraph("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc at turpis nec nisl lobortis convallis. Curabitur nec ipsum mauris."));
        onboardingStep3.setContent(content);
        onboardingStep3.getContent().getElement().getStyle().set("max-width", "30rem");

        return onboardingStep3;
    }

    private OnboardingStep createStep2(Paragraph paragraph) {
        final OnboardingStep onboardingStep2 = new OnboardingStep(paragraph);
        onboardingStep2.setHeader("Bottom positioned");
        onboardingStep2.setContent("Popup for this step is positioned to the bottom of the target element");
        onboardingStep2.setPosition(PopupPosition.BOTTOM);
        return onboardingStep2;
    }

    private OnboardingStep createStep1(H3 header) {
        final OnboardingStep onboardingStep1 = new OnboardingStep(header);
        onboardingStep1.setHeader("Start here");
        onboardingStep1.setContent("You can associate the Onboarding Step with any Component on the screen. Note: This popup is customized using the addBeforePopupShown listener - 'More info' button is added to the footer.");
        onboardingStep1.addBeforePopupShownListener(popup -> {
            final Button moreInfo = new Button("More info");
            moreInfo.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            moreInfo.getElement().getStyle().set("position", "absolute");
            moreInfo.getElement().getStyle().set("left", "var(--lumo-space-l)");
            moreInfo.addClickListener(event -> Notification.show("Some action"));
            popup.getFooter().add(moreInfo);
        });
        return onboardingStep1;
    }

}