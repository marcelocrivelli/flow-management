package com.proyecto.flowmanagement.ui.views.pages;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Operation;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.proyecto.flowmanagement.ui.views.grids.OperationGridForm;
import com.proyecto.flowmanagement.ui.views.panels.*;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.LinkedList;
import java.util.List;

@org.springframework.stereotype.Component
@Route(value = "CrearGuia", layout = MainLayout.class)
@PageTitle("Crear Guia | Flow Management")
public class GuideCreator extends VerticalLayout {

    HorizontalLayout actualPanelLaayout;
    HorizontalLayout detailsPanelLayout;
    HorizontalLayout stepPanelLayout;
    HorizontalLayout operationPanelLayout;
    HorizontalLayout guidePanelLayout;

    ActualGuidePanel actualGuidePanel;
    OperationGridForm operationGridForm;
    DetailsPanel detailsPanel;
    StepPanel stepPanel;
    GuidePanel guidePanel;

    public Button save = new Button("Guardar");
    public Button validar = new Button("Validar");

    Guide raiz;
    Guide actual;
    Guide editado;

    public GuideCreator()
    {
        setSizeFull();
        
        configureActualGuidePanel();

        configureActions();

        configureElements();

        configureDetailsPanel();

        configureGuidePanel();

        configureStepPanel();

        configureOperatorPanel();

        configureInteractivitie();

        configureForm();
    }

    private void configureActions() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        validar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(buttonClickEvent -> guardarGuias());
    }

    private void guardarGuias() {
    }

    private void configureActualGuidePanel()
    {
        actualPanelLaayout = new HorizontalLayout();
        actualPanelLaayout.setWidthFull();
        actualGuidePanel = new ActualGuidePanel();
        actualPanelLaayout.add(actualGuidePanel);
        actualPanelLaayout.setVisible(false);
    }

    private void configureElements() {
        editado = new Guide();
        raiz = new Guide();
        actual = raiz;
    }

    private void configureGuidePanel() {
        guidePanelLayout = new HorizontalLayout();
        guidePanelLayout.setWidthFull();
        guidePanel = new GuidePanel();
        guidePanelLayout.add(guidePanel);
    }

    private void configureDetailsPanel() {
        detailsPanelLayout = new HorizontalLayout();
        detailsPanelLayout.setWidthFull();
        detailsPanel = new DetailsPanel();
        detailsPanelLayout.add(detailsPanel);
    }

    private void configureStepPanel() {
        stepPanelLayout = new HorizontalLayout();
        stepPanelLayout.setWidthFull();
        stepPanel = new StepPanel();
        stepPanelLayout.add(stepPanel);
    }

    private void configureOperatorPanel() {
        operationGridForm = new OperationGridForm();
        operationGridForm.setWidthFull();

        operationPanelLayout = new HorizontalLayout();
        operationPanelLayout.setWidthFull();

        operationPanelLayout.add(operationGridForm);
    }

    private void configureInteractivitie() {
        actualizacionDeDetalles();
        actualizacionAlternativs();
        actualizacionCrecionStep();
        cambioGuia();
        //Actualizar Steps
        // Alternativs
        //Actuaalizar resumen
    }

    private void actualizacionCrecionStep()
    {
        stepPanel.stepGridForm.stepForm.save.addClickListener(buttonClickEvent -> cargarMainSteps());
    }

    private void cargarMainSteps() {

        if(stepPanel.stepGridForm.stepForm.esValido)
        {
            guidePanel.actualizarSteps(stepPanel.stepGridForm.stepList);
        }
    }


    private void cambioGuia()
    {
        actualGuidePanel.actualGuide.addValueChangeListener(event -> cargarGuia());
    }

    private void cargarGuia() {
        Guide valor = actualGuidePanel.actualGuide.getValue();
        if(valor != null)
        {
            actual = valor;
            editado = new Guide();
            cargarValorGrilla(valor);
        }
    }

    private void cargarValorGrilla(Guide guide)
    {
        actualizarGuiaActual();

        this.stepPanel.stepGridForm.stepList = guide.getSteps();
        this.stepPanel.stepGridForm.updateGrid();

        this.operationGridForm.operationList = guide.getOperations();
        this.operationGridForm.updateGrid();

        this.guidePanel.actualizarAtributos(guide);
        this.guidePanel.name.setValue(guide.getName());
    }

    private void actualizacionAlternativs()
    {
        stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.save.addClickListener(buttonClickEvent -> nuevoStep());
    }

    private void nuevoStep() {

        if(stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.isValid)
        {
            Alternative myAlternative = stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.getAlternative();

            if(myAlternative.getNewStep())
            {
                List<Step> stepsActuales = stepPanel.stepGridForm.getSteps();

                boolean noExiste = stepsActuales.stream().filter(aux -> aux.getTextId().equals(myAlternative.getNextStep())).count() == 0;

                if(noExiste)
                {
                    Step nuevo = new Step();
                    nuevo.setText(myAlternative.getNextStep());
                    nuevo.setLabel(myAlternative.getNextStep());
                    nuevo.setTextId(myAlternative.getNextStep());
                    this.stepPanel.stepGridForm.stepList.add(nuevo);
                    this.stepPanel.stepGridForm.updateGrid();
                    actualizarGuiaActual();
                }
            }
            else if( myAlternative.getGuideName() != null ||  !myAlternative.getGuideName().isEmpty())
            {
                Guide newGuide = new Guide();
                newGuide.setName(myAlternative.getGuideName());

                if(editado.getGuides()== null)
                    editado.setGuides(new LinkedList<>());

                editado.addGuide(newGuide);

                actualizarGuiaActual();
            }
        }
    }

    private void actualizarListaDeGuias(Guide actual)
    {
        if(actual.getGuides().size() > 1)
        {
            actualGuidePanel.setVisible(true);
            actualGuidePanel.setItems(actual.getGuides());
            actualGuidePanel.actualGuide.setValue(actual);
        }
        else
        {
            actualGuidePanel.setVisible(false);
        }
    }

    private void configureForm() {
        add(actualPanelLaayout,detailsPanelLayout,detailsPanel,guidePanelLayout,stepPanelLayout,operationPanelLayout);
    }

    private void actualizacionDeDetalles() {
        stepPanel.stepGridForm.stepForm.save.addClickListener( buttonClickEvent ->  actualizarGuiaActual());
        stepPanel.stepGridForm.stepForm.delete.addClickListener( buttonClickEvent ->  actualizarGuiaActual());
        guidePanel.name.addValueChangeListener(addListener ->  actualizarGuiaActual());
    }

    private void actualizarGuiaActual()
    {
        // Elementos Guia
        editado.setName(guidePanel.name.getValue());
        editado.setLabel(guidePanel.label.getValue());

        if(guidePanel.mainStep.getValue() != null)
            editado.setMainStep(guidePanel.mainStep.getValue());
        else
            editado.setMainStep("");

        //Elementos de Steps
        List<Step> stepList = stepPanel.stepGridForm.getSteps();
        editado.setSteps(stepList);

        //ElementosOperations
        List<Operation> operationsList = operationGridForm.getOperations();
        editado.setOperations(operationsList);

        if(raiz == actual)
            raiz = editado;
        else
         raiz.setGuide(actual, editado);

        actual = editado;

        detailsPanel.updateGridDetails(raiz);

        if(raiz.getGuides() != null && raiz.getGuides().size() > 0)
            mostrarOpcionesDeGuias();
        else
            actualPanelLaayout.setVisible(false);
    }

    private void mostrarOpcionesDeGuias()
    {
        actualPanelLaayout.setVisible(true);
        actualGuidePanel.actualizarItems(raiz);
    }

}




