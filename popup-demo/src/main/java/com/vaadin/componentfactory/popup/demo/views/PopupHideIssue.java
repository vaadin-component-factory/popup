package com.vaadin.componentfactory.popup.demo.views;

import com.vaadin.componentfactory.Popup;
import com.vaadin.componentfactory.PopupVariant;
import com.vaadin.componentfactory.popup.demo.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value="popup-hide-issue" , layout = MainLayout.class)
public class PopupHideIssue extends VerticalLayout {
	public PopupHideIssue() {
		Button filterButton = new Button("filter");
		filterButton.setId("filter-button-main");
		Popup popup = new Popup();
		Button popupButton = new Button("popup button");
		popupButton.addClickListener(event -> {
			popup.hide();
		});
		popup.add(popupButton);
		popup.setFor("filter-button-main");
		popup.addThemeVariants(PopupVariant.LUMO_POINTER_ARROW);

		Button closePopupButton = new Button("close popup button");
		closePopupButton.addClickListener(event -> {
			popup.hide();
		});
		add(filterButton, popup, closePopupButton);
		
	}

}
