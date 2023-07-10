package com.vaadin.componentfactory.vaadincom;

import com.vaadin.componentfactory.PopupPosition;
import com.vaadin.componentfactory.onboarding.Onboarding;
import com.vaadin.componentfactory.onboarding.OnboardingStep;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "onboarding", layout = OnboardingLayout.class)
public class OnboardingView extends VerticalLayout {

    public OnboardingView() {
        final H3 header = new H3("Lorem Ipsum");
        add(header);

        final Paragraph paragraph = new Paragraph("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publis");
        add(paragraph);

        final Button makePurchase = new Button("Make purchase");
        add(makePurchase);

        Onboarding onboarding = createOnboarding(header, paragraph, makePurchase);
        final Button startOnboarding = new Button("Start onboarding", event -> {
            onboarding.start();
        });
        startOnboarding.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(startOnboarding);
    }

    private Onboarding createOnboarding(H3 header, Paragraph paragraph, Button makePurchase) {
        Onboarding onboarding = new Onboarding();

        onboarding.getSteps().add(createStep1(header));
        onboarding.getSteps().add(createStep2(paragraph));
        onboarding.getSteps().add(createStep3(makePurchase));
        onboarding.getSteps().add(createStep4());

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
        onboardingStep3.setHeader("Long text popup - having max-width");
        onboardingStep3.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc at turpis nec nisl lobortis convallis. Curabitur nec ipsum mauris. Proin vitae consectetur justo, sit amet ultricies ex. Proin posuere sem vel nunc lobortis lacinia. Quisque est felis, gravida in hendrerit sagittis, egestas tempor ante. Etiam vulputate urna lorem, eget rutrum sem auctor eget. Aliquam fringilla ut lacus a consectetur. Ut congue vitae quam nec suscipit. Mauris lacinia libero ex, non porttitor nisi elementum sit amet.");
        onboardingStep3.getContent().getElement().getStyle().set("max-width", "40rem");
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
        onboardingStep1.setContent("This popup is customized using the addBeforePopupShown listener - 'More info' button is added to the footer.");
        onboardingStep1.addBeforePopupShownListener(popup -> {
            final Button moreInfo = new Button("More info");
            moreInfo.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            moreInfo.getElement().getStyle().set("position", "absolute");
            moreInfo.getElement().getStyle().set("left", "var(--lumo-space-l)");
            moreInfo.addClickListener(event -> {
                Notification.show("Some action");
            });
            popup.getFooter().add(moreInfo);
        });
        return onboardingStep1;
    }

}