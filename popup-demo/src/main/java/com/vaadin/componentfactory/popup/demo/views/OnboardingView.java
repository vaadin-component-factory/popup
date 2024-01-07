package com.vaadin.componentfactory.popup.demo.views;

import com.vaadin.componentfactory.PopupPosition;
import com.vaadin.componentfactory.onboarding.Onboarding;
import com.vaadin.componentfactory.onboarding.OnboardingStep;
import com.vaadin.componentfactory.popup.demo.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "onboarding", layout = MainLayout.class)
public class OnboardingView extends VerticalLayout {

    private Paragraph topParagraph;
    private Paragraph outOfViewportParagraph;
    private Button actionButton;
    private Button startOnboardingButton;
    private H3 header;

    public OnboardingView() {
        createUI();

        Onboarding onboarding = createOnboarding();
        startOnboardingButton.addClickListener(event -> onboarding.start());
    }

    private void createUI() {
        header = new H3("How to use Popup to create an onboarding experience");
        add(header);

        topParagraph = new Paragraph("The idea is to use the Popup component to create a simple" +
                " Onboarding (also known as Walkthrough) experience for the  user. A simple API was created as part of " +
                "the component to make it easy to create such onboarding. There are two classes: Onboarding and OnboardingStep. " +
                "To use it, create an instance of the Onboarding class and fill it with the OnboardingSteps. In the simplest form, " +
                "you only have to provide three things for each OnboardingStep: a target element, a header text, and a content text.");
        add(topParagraph);

        actionButton = new Button("Some Action");
        add(actionButton);

        startOnboardingButton = new Button("Start onboarding");
        startOnboardingButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(startOnboardingButton);

        final Div spacer = new Div();
        spacer.getStyle().set("height", "900px");
        add(spacer);

        outOfViewportParagraph = new Paragraph("You have to scroll to see this text.");
        add(outOfViewportParagraph);
    }

    private Onboarding createOnboarding() {
        Onboarding onboarding = new Onboarding();

        onboarding.addStep(createStepHeader());
        onboarding.addStep(createStepTopParagraph());
        onboarding.addStep(createStepOutOfViewParagraph());
        onboarding.addStep(createStepActionButton());
        onboarding.addStep(createStepNoTarget());

        return onboarding;
    }

    private OnboardingStep createStepOutOfViewParagraph() {
        final OnboardingStep step = new OnboardingStep(outOfViewportParagraph);
        step.setHeader("Scroll to popup target");
        step.setContent("When a target of the onboarding step is not visible, the screen " +
                "will be scrolled to make it visible before the popup opens.");
        step.setPosition(PopupPosition.BOTTOM);
        return step;
    }

    private OnboardingStep createStepNoTarget() {
        final OnboardingStep step = new OnboardingStep(null);
        step.setHeader("No target");
        step.setContent("This onboarding step does not have any related target element");
        return step;
    }

    private OnboardingStep createStepActionButton() {
        final OnboardingStep step = new OnboardingStep(actionButton);
        step.setHeader("Popup with rich content");

        VerticalLayout content = new VerticalLayout();
        content.setWidthFull();
        content.add(new Paragraph("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc at turpis nec nisl lobortis convallis. Curabitur nec ipsum mauris. Proin vitae consectetur justo, sit amet ultricies ex. Proin posuere sem vel nunc lobortis lacinia. Quisque est felis, gravida in hendrerit sagittis, egestas tempor ante. Etiam vulputate urna lorem, eget rutrum sem auctor eget. Aliquam fringilla ut lacus a consectetur. Ut congue vitae quam nec suscipit. Mauris lacinia libero ex, non porttitor nisi elementum sit amet."));
        final Image vaadinLogo = new Image("https://vaadin.com/images/trademark/PNG/VaadinLogo_RGB_500x155.png", "Vaadin Logo");
        vaadinLogo.setWidth("15rem");
        content.add(vaadinLogo);
        content.add(new Paragraph("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc at turpis nec nisl lobortis convallis. Curabitur nec ipsum mauris."));
        step.setContent(content);
        step.getContent().getElement().getStyle().set("max-width", "30rem");

        return step;
    }

    private OnboardingStep createStepTopParagraph() {
        final OnboardingStep step = new OnboardingStep(topParagraph);
        step.setHeader("Bottom positioned");
        step.setContent("Popup for this step is positioned to the bottom of the target element");
        step.setPosition(PopupPosition.BOTTOM);
        return step;
    }

    private OnboardingStep createStepHeader() {
        final OnboardingStep step = new OnboardingStep(header);
        step.setHeader("Start here");
        step.setContent("You can associate the Onboarding Step with any Component on the screen. Note: This popup is customized using the addBeforePopupShown listener - 'More info' button is added to the footer.");
        step.addBeforePopupShownListener(popup -> {
            final Button moreInfo = new Button("More info");
            moreInfo.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            moreInfo.getElement().getStyle().set("position", "absolute");
            moreInfo.getElement().getStyle().set("left", "var(--lumo-space-l)");
            moreInfo.addClickListener(event -> Notification.show("Some action"));
            popup.getFooter().add(moreInfo);
        });
        return step;
    }

}