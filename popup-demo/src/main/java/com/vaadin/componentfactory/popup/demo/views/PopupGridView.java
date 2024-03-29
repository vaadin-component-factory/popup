package com.vaadin.componentfactory.popup.demo.views;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.vaadin.componentfactory.ComponentWithPopupRenderer;
import com.vaadin.componentfactory.Popup;
import com.vaadin.componentfactory.PopupAlignment;
import com.vaadin.componentfactory.PopupPosition;
import com.vaadin.componentfactory.PopupVariant;
import com.vaadin.componentfactory.popup.demo.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;

@Route(value = "popup-grid", layout = MainLayout.class)
public class PopupGridView extends VerticalLayout {

    private boolean closePopupsOnScroll = true;

    public PopupGridView() {
        add(new H3("Popup used in Grid rows"));

        add(new Paragraph("This page demonstrates the possibility of using the Popup in the Grid. You can use " +
                "ComponentWithPopupRenderer class to generate Grid columns with Popups like in the " +
                "Grid below. Click on the content of the 'ID' column to open the Popup. You can " +
                "also use the keyboard navigation in the Grid and open the Popup using Spacebar."));

        addGrid();
        addCloseOnScrollToggleButton();
    }

    private void addCloseOnScrollToggleButton() {
        Span status = new Span(closeOnScrollStatusText());

        Button button = new Button("Toggle close popup on scroll");
        button.addClickListener(event -> {
            this.closePopupsOnScroll = !this.closePopupsOnScroll;
            status.setText(closeOnScrollStatusText());
        });

        final HorizontalLayout layout = new HorizontalLayout(button, status);
        layout.setAlignItems(Alignment.CENTER);
        add(layout);
    }

    private String closeOnScrollStatusText() {
        return "Popup `closeOnScroll` property value: " + this.closePopupsOnScroll;
    }

    private void addGrid() {
        Grid<Person> grid = createGrid();

        addIdWithPopupColumn(grid);
        addFirstNameColumn(grid);
        addLastNameColumn(grid);
        addPersonTypeColumn(grid);

        grid.setItems(createExampleData());
    }

    private void addIdWithPopupColumn(Grid<Person> grid) {
        grid.addColumn(createButtonWithPopupRenderer()).setHeader("ID");
    }

    private ComponentWithPopupRenderer<Person> createButtonWithPopupRenderer() {
        return new ComponentWithPopupRenderer<>(
                this::createButtonWithPersonId,
                this::createPopupForPersonId
        );
    }

    private Popup createPopupForPersonId(Person item) {
        Popup popup = new Popup();
        popup.setModeless(true);
        popup.setFocusTrap(true);
        popup.addThemeVariants(PopupVariant.LUMO_POINTER_ARROW);
        popup.setPosition(PopupPosition.END);
        popup.setAlignment(PopupAlignment.CENTER);
        popup.setCloseOnScroll(this.closePopupsOnScroll);
        popup.setHeaderTitle(String.format("Popup %d", item.getId()));
        popup.getHeader().add(createPopupCloseButton(popup));

        VerticalLayout content = new VerticalLayout();
        content.add(new Span(String.format("Popup %d content", item.getId())));
        content.add("Will close on scroll?: " + this.closePopupsOnScroll);
        popup.add(content);

        return popup;
    }

    private Button createPopupCloseButton(Popup popup) {
        Button closeBtn = new Button(LumoIcon.CROSS.create());
        closeBtn.addClickListener(e -> popup.hide());
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return closeBtn;
    }

    private Component createButtonWithPersonId(Person person) {
        Button button = new Button(String.valueOf(person.getId()));
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        return button;
    }

    private void addLastNameColumn(Grid<Person> grid) {
        grid.addColumn(Person::getLastName).setHeader("Last name");
    }

    private void addFirstNameColumn(Grid<Person> grid) {
        grid.addColumn(Person::getLastName).setHeader("Last name");
        grid.addColumn(Person::getFirstName).setHeader("First name");
    }

    private void addPersonTypeColumn(Grid<Person> grid) {
        grid.addColumn(Person::getType).setHeader("Person type");
    }

    private List<Person> createExampleData() {
        return IntStream.range(1, 30)
                .mapToObj(Person::new)
                .collect(Collectors.toList());
    }

    private Grid<Person> createGrid() {
        Grid<Person> grid = new Grid<>();
        grid.setHeight("400px");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        add(grid);

        return grid;
    }

    @SuppressWarnings("unused")
    public static class Person {
        private Integer id;
        private String type;
        private String firstName;
        private String lastName;
        private String placeholder;
        private String actions;

        public Person(int id) {
            this.id = id;
            type = "Person Type " + id;
            firstName = "First Name " + id;
            lastName = "Last Name " + id;
            placeholder = "";
            actions = "X";
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPlaceholder() {
            return placeholder;
        }

        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }

        public String getActions() {
            return actions;
        }

        public void setActions(String actions) {
            this.actions = actions;
        }

    }

}