package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.ui.views.forms.AlternativeForm;
import com.proyecto.flowmanagement.ui.views.forms.StepForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/general.css")
public class AlternativeGridForm extends VerticalLayout {

    private AlternativeForm alternativeForm;
    public Button createAlternative;
    Alternative editAlternative;
    Grid<Alternative> alternativeGrid;
    List<Alternative> alternativeList;

    public AlternativeGridForm()
    {
        setSizeFull();
        configureElements();
    }

    private void configureElements()
    {
        this.alternativeList = new LinkedList<>();
        configureGrid();
        createAlternative = new Button("Crear Alternative", click -> addAlternative());

        alternativeForm = new AlternativeForm();
        alternativeForm.setVisible(false);
        alternativeForm.save.addClickListener(buttonClickEvent -> CreateorSaveAlternative());
        alternativeForm.close.addClickListener(buttonClickEvent -> CloseForm());

        setWidthFull();
        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(alternativeGrid);
        gridLayout.addClassName("gridHorizontalLayout");

        HorizontalLayout alternativeFormLayout = new HorizontalLayout();
        alternativeFormLayout.add(alternativeForm);
        alternativeFormLayout.setWidthFull();

        HorizontalLayout createAlternativeLyout = new HorizontalLayout();
        createAlternativeLyout.add(createAlternative);
        createAlternativeLyout.setWidthFull();

        add(createAlternativeLyout, alternativeFormLayout, gridLayout);
    }

    private void CloseForm() {
        this.alternativeForm.setVisible(false);
    }

    private void CreateorSaveAlternative() {

        Alternative newAlternative = alternativeForm.getAlternative();

        if(alternativeForm.editing)
        {
            int index = alternativeList.indexOf(editAlternative);
            alternativeList.set(index, newAlternative);
            updateGrid();
        }
        else
        {
            alternativeList.add(newAlternative);
            updateGrid();
            closeEditor();
        }
    }

    private void updateGrid() {
        alternativeGrid.setItems(alternativeList);
    }

    private void addAlternative() {
        alternativeGrid.asSingleSelect().clear();
        editAlternative(null);
    }

    private void configureGrid() {
        alternativeGrid = new Grid<>(Alternative.class);
        alternativeGrid.addClassName("user-grid");
        alternativeGrid.setSizeFull();
        alternativeGrid.setColumns("label","nextStep");
        alternativeGrid.asSingleSelect().addValueChangeListener(evt -> editAlternative(evt.getValue()));
    }

    private void editAlternative(Alternative alternative) {
        alternativeForm.setVisible(true);

        if(alternative != null) {
            this.editAlternative = alternative;
            alternativeForm.setAlternative(alternative);
            addClassName("editing");
        }
        else
        {
            alternativeForm.setAlternative(null);
        }
    }

    private void closeEditor() {
        alternativeForm.setAlternative(null);
        alternativeForm.setVisible(false);
        removeClassName("editing");
    }

    public List<Alternative> getAlternatives() {
        return this.alternativeList;
    }
}