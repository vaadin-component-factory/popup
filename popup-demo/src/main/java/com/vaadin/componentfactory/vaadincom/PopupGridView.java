package com.vaadin.componentfactory.vaadincom;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.vaadin.componentfactory.ButtonWithPopupRenderer;
import com.vaadin.componentfactory.ComponentWithPopupRenderer;
import com.vaadin.componentfactory.Popup;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;

@Route("popup-grid")
public class PopupGridView extends VerticalLayout {

    public PopupGridView() {
        initGrid();
    }

    private void initGrid() {
        Grid<Person> grid = createGrid();

        addIdWithPopupColumn(grid);
        addFirstNameWithPopupColumn(grid);
        addLastNameColumn(grid);
        addPersonTypeColumn(grid);

        grid.setItems(createExampleData());
    }

    private void addIdWithPopupColumn(Grid<Person> grid) {
        grid.addColumn(createButtonWithPopupRenderer()).setHeader("ID");
    }

    private ButtonWithPopupRenderer<Person> createButtonWithPopupRenderer() {
        return new ButtonWithPopupRenderer<>(
                this::getPersonIdAsString,
                this::createPopupForPersonId
        );
    }

    private Popup createPopupForPersonId(Person item) {
        Popup popup = new Popup();
        popup.setModeless(true);
        popup.setCloseOnScroll(true);
        popup.setHeaderTitle(String.format("Popup %d", item.getId()));
        popup.getHeader().add(createPopupCloseButton(popup));

        VerticalLayout content = new VerticalLayout();
        content.add(new Span(String.format("Popup %d content", item.getId())));
        popup.add(content);

        return popup;
    }

    private Button createPopupCloseButton(Popup popup) {
        Button closeBtn = new Button(LumoIcon.CROSS.create());
        closeBtn.addClickListener(e -> popup.hide());
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return closeBtn;
    }

    private String getPersonIdAsString(Person person) {
        return String.valueOf(person.getId());
    }

    private void addLastNameColumn(Grid<Person> grid) {
        grid.addColumn(Person::getLastName).setHeader("Last name");
    }

    private void addFirstNameWithPopupColumn(Grid<Person> grid) {
        grid.addColumn(createComponentWithPopupRenderer()).setHeader("First name");
    }

    private ComponentWithPopupRenderer<Person> createComponentWithPopupRenderer() {
        return new ComponentWithPopupRenderer<>(
                this::createSpanWithFirstName,
                this::createPopupForPersonId
        );
    }

    private Component createSpanWithFirstName(Person item) {
        return new Span(item.firstName);
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