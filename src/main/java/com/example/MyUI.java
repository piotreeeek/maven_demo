package com.example;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Piotrek on 2017-04-15.
 */
@Theme(ValoTheme.THEME_NAME)
@SpringUI
public class MyUI extends UI {
    @Autowired
    PersonRepo repo;
    BeanItemContainer<Person> container;
    HorizontalLayout layout;
    Grid personGrid;
    TextField nameTextField;
    TextField emailTextField;
    Button submit;
    Button delete;
    Person editPerson;
    @Override
    protected void init(VaadinRequest request) {
        layout = new HorizontalLayout();
        initGrid();
        initForm();
        setContent(layout);
    }
    private void initForm() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        nameTextField = new TextField();
        nameTextField.setCaption("Name");
        emailTextField = new TextField();
        emailTextField.setCaption("Email");
        submit = new Button();
        delete = new Button();
        delete.setCaption("Delete");
        submit.setCaption("Add/Modify");
        delete.setEnabled(false);
        submit.addClickListener(clickEvent -> {
            if (editPerson.getId()>0) {
                editPerson.setEmail(emailTextField.getValue());
                editPerson.setName(nameTextField.getValue());
                repo.save(editPerson);
                editPerson = new Person();
            }else {
                Person newPerson = new Person();
                newPerson.setEmail(emailTextField.getValue());
                newPerson.setName(nameTextField.getValue());
                repo.save(newPerson);
            }

            container = new BeanItemContainer<Person>(Person.class,
                    repo.findAll());
            personGrid.setContainerDataSource(container);
        });
        delete.addClickListener(clickEvent -> {
            repo.delete(editPerson);
            delete.setEnabled(false);
            editPerson = new Person();

            container = new BeanItemContainer<Person>(Person.class,
                    repo.findAll());
            personGrid.setContainerDataSource(container);
        });
        verticalLayout.addComponents(nameTextField, emailTextField, submit, delete);
        layout.addComponent(verticalLayout);
    }
    private void initGrid() {
        List<Person> people = repo.findAll();
        container = new BeanItemContainer<Person>(Person.class, people);
        personGrid = new Grid(container);
        personGrid.setSizeFull();
        personGrid.addSelectionListener(selection-> {
            delete.setEnabled(true);
            editPerson = (Person) selection.getSelected().iterator().next();
            nameTextField.setValue(editPerson.getName());
            emailTextField.setValue(editPerson.getEmail());
        });
        layout.addComponent(personGrid);
    }
}