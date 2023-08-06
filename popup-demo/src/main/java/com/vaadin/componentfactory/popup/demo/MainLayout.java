package com.vaadin.componentfactory.popup.demo;

import com.vaadin.componentfactory.popup.demo.views.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    public MainLayout() {
        final DrawerToggle drawerToggle = new DrawerToggle();

        final RouterLink basicUsages = new RouterLink("Popup basic usage", PopupBasicUsageView.class);
        final RouterLink headerAndFooter = new RouterLink("Header and Footer", HeaderAndFooterView.class);
        final RouterLink popupInGrid = new RouterLink("Popup in Grid", PopupGridView.class);
        final RouterLink onboardingDemo = new RouterLink("Onboarding Demo", OnboardingView.class);

        final VerticalLayout menuLayout = new VerticalLayout(basicUsages, headerAndFooter, popupInGrid, onboardingDemo);
        addToDrawer(menuLayout);
        addToNavbar(drawerToggle);
    }

}