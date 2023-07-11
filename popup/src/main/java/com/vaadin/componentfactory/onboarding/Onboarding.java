package com.vaadin.componentfactory.onboarding;

import com.vaadin.componentfactory.Popup;
import com.vaadin.componentfactory.PopupVariant;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.theme.lumo.LumoIcon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a helper class, which makes it easier to use Popup component as part of the "Onboarding walkthrough".
 * Onboarding guides users through your app. It uses Popup to render "dialogs" for each tour "step", showing
 * Next, Previous and Close buttons.
 * <p>
 * Use {@link OnboardingStep} to define each step of the onboarding. You can then use {@code Onboarding.start()} and
 * {@code Onboarding.stop()} to control the walkthrough.
 * <p>
 * Closing any Popup using any method stops the walkthrough.
 */
public class Onboarding implements Serializable {

    public static final String PREVIOUS_STEP_BUTTON_ID = "previous-step-button";
    public static final String NEXT_STEP_BUTTON_ID = "next-step-button";
    public static final String CLOSE_BUTTON_HEADER_ID = "close-button-header";
    public static final String CLOSE_BUTTON_FOOTER_ID = "close-button-footer";

    private final List<OnboardingStep> steps = new ArrayList<>();
    private int currentStep;
    private Popup currentPopup;
    private boolean isSwitchingSteps;

    public List<OnboardingStep> getSteps() {
        return steps;
    }

    public void start() {
        if (!isRunning()) {
            currentStep = -1;
            showNextStep();
        }
    }

    public void stop() {
        closeCurrentPopup();
    }

    public boolean isRunning() {
        return this.currentPopup != null && this.currentPopup.isOpened();
    }

    public void addStep(OnboardingStep step) {
        getSteps().add(step);
    }

    protected void showNextStep() {
        if (!isLastStep()) {
            whenSwitchingSteps(() -> {
                closeCurrentPopup();
                currentStep++;
                showPopupForCurrentStep();
            });
        }
    }

    private void whenSwitchingSteps(Runnable action) {
        isSwitchingSteps = true;
        try {
            action.run();
        } finally {
            isSwitchingSteps = false;
        }
    }

    private void showPopupForCurrentStep() {
        final OnboardingStep onboardingStep = steps.get(currentStep);
        currentPopup = createPopup(onboardingStep);
        if (onboardingStep.getTargetElement() != null) {
            onboardingStep.getTargetElement().getParent().ifPresent(parent -> parent.getElement().appendChild(currentPopup.getElement()));
        } else {
            UI.getCurrent().add(currentPopup);
        }
        onboardingStep.fireBeforePopupShown(currentPopup);
        currentPopup.show();
    }

    private void closeCurrentPopup() {
        if (currentPopup != null) {
            currentPopup.hide();
            currentPopup.getElement().removeFromParent();
        }
    }

    protected void showPreviousStep() {
        if (!isFirstStep()) {
            whenSwitchingSteps(() -> {
                closeCurrentPopup();
                currentStep--;
                showPopupForCurrentStep();
            });
        }
    }

    protected Popup createPopup(OnboardingStep onboardingStep) {
        Popup popup = new Popup();
        popup.addThemeVariants(PopupVariant.LUMO_POINTER_ARROW);
        popup.setHighlightTarget(true);
        popup.setPosition(onboardingStep.getPosition());
        popup.setAlignment(onboardingStep.getAlignment());
        popup.setIgnoreTargetClick(true);
        if (onboardingStep.getTargetElement() != null) {
            popup.setTarget(onboardingStep.getTargetElement().getElement());
        }

        setupPopupHeader(onboardingStep, popup);
        setupPopupFooter(popup);
        setupStopOnPopupClose(popup);

        popup.add(onboardingStep.getContent());

        return popup;
    }

    private void setupStopOnPopupClose(Popup popup) {
        popup.addPopupOpenChangedEventListener(event -> {
            if (!event.isOpened() && !this.isSwitchingSteps) {
                // user clicked outside Popup -> stop the Onboarding
                stop();
            }
        });
    }

    protected void setupPopupHeader(OnboardingStep onboardingStep, Popup popup) {
        if (onboardingStep.getHeader() != null) {
            popup.setHeaderTitle(onboardingStep.getHeader());
        } else {
            // so the close button is aligned to the right
            popup.getHeader().getElement().getStyle().set("width", "100%");
            popup.getHeader().getElement().getStyle().set("display", "flex");
            popup.getHeader().getElement().getStyle().set("justify-content", "end");
        }
        popup.getHeader().add(createPopupCloseButton(popup));
    }

    protected void setupPopupFooter(Popup popup) {
        addPreviousStepButton(popup);
        addNextStepButton(popup);
        addCloseOnboardingButton(popup);
    }

    protected void addCloseOnboardingButton(Popup popup) {
        if (isLastStep()) {
            Button closeButton = createCloseOnboardingButton();
            popup.getFooter().add(closeButton);
        }
    }

    protected void addNextStepButton(Popup popup) {
        if (!isLastStep()) {
            Button nextButton = createNextStepButton();
            popup.getFooter().add(nextButton);
        }
    }

    protected void addPreviousStepButton(Popup popup) {
        if (!isFirstStep()) {
            Button previousButton = createPreviousStepButton();
            popup.getFooter().add(previousButton);
        }
    }

    protected Button createCloseOnboardingButton() {
        Button closeButton = new Button("Close");
        closeButton.setId(CLOSE_BUTTON_FOOTER_ID);
        closeButton.addClickListener(e -> stop());
        return closeButton;
    }

    protected Button createNextStepButton() {
        Button nextButton = new Button("Next");
        nextButton.setId(NEXT_STEP_BUTTON_ID);
        nextButton.addClickListener(e -> showNextStep());
        return nextButton;
    }

    protected Button createPreviousStepButton() {
        Button previousButton = new Button("Previous");
        previousButton.setId(PREVIOUS_STEP_BUTTON_ID);
        previousButton.addClickListener(e -> showPreviousStep());
        return previousButton;
    }

    protected boolean isFirstStep() {
        return currentStep == 0;
    }

    protected boolean isLastStep() {
        return currentStep == steps.size() - 1;
    }

    protected Button createPopupCloseButton(Popup popup) {
        Button closeBtn = new Button(LumoIcon.CROSS.create());
        closeBtn.setId(CLOSE_BUTTON_HEADER_ID);
        closeBtn.addClickListener(e -> popup.hide());
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
        return closeBtn;
    }

}
