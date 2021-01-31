package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.proyecto.flowmanagement.ui.views.grids.OperationGridForm;
import com.proyecto.flowmanagement.ui.views.grids.StepGridForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

import java.util.LinkedList;
import java.util.List;

@org.springframework.stereotype.Component
@Route(value = "CreateGuide", layout = MainLayout.class)
@PageTitle("CreateGuide | Flow Management")
public class GuideForm extends VerticalLayout {

    private Guide guide;

    HorizontalLayout guideLayout;
    HorizontalLayout stepSecctionLayout;
    HorizontalLayout conditionsSecctionLayout;
    HorizontalLayout operationSecctionLayout;
    HorizontalLayout actionsLayout;
    HorizontalLayout guideDetailsLayout;

    Button crearGuia;

    HorizontalLayout layoutGuide;
    TextField name;
    TextField label;
    ComboBox<String> mainStep;

    StepGridForm stepGridForm;
    OperationGridForm operationGridForm;
    GuideDetailsForm guideDetailsForm = new GuideDetailsForm();

    public GuideForm(GuideServiceImpl guideService)
    {
        configureGuideDetails();

        configureGuideElements();

        configureStepElements();

        configureOperationElements();
        
        configureDocumentsElements();

        createForm();
    }

    private void configureGuideDetails() {
        guideDetailsLayout = new HorizontalLayout();
        guideDetailsLayout.setSizeFull();
        guideDetailsLayout.add(guideDetailsForm);
    }


    private void configureDocumentsElements() {
    }

    private void configureOperationElements() {
        operationGridForm = new OperationGridForm();
        operationGridForm.setWidthFull();

        operationSecctionLayout = new HorizontalLayout();
        operationSecctionLayout.setWidthFull();

        operationSecctionLayout.add(operationGridForm);
    }

    private void createForm() {
        crearGuia = new Button("Crear Guia");
//        crearGuia.addClickListener(buttonClickEvent -> crearGuia());
        crearGuia.addClickListener(buttonClickEvent -> validateAndSave());
        HorizontalLayout botonCrear = new HorizontalLayout();

        Span content = new Span("Guia creada y archivo XML generado!");
        Notification notification = new Notification(content);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);

        Button button = new Button("Crear Guia");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        button.addClickListener(event -> notification.open());
        botonCrear.add(button);
        add(guideDetailsLayout,guideLayout,stepSecctionLayout,operationSecctionLayout,botonCrear);
    }

    private void validateAndSave() {
        if(isValid())
        {
            guide.setLabel(label.getValue());
            guide.setName(name.getValue());
            guide.setSteps(stepGridForm.getSteps());
            guide.setOperations(operationGridForm.getOperations());
        }
    }

    private boolean isValid() {
        boolean result = false;

        if(validateFields())
            result = true;

        return result;
    }

    public boolean validateFields(){
        boolean result = false;

        if(!name.getValue().isEmpty() &&
                !label.getValue().isEmpty() &&
                !mainStep.isEmpty())
            result = true;

        return result;
    }


    private void crearGuia() {

        Guide newGuide = new Guide();

        newGuide.setName(name.getValue());
        newGuide.setLabel(label.getValue());
        newGuide.setMainStep(mainStep.getValue());

        newGuide.setSteps(stepGridForm.getSteps());
        newGuide.setOperations(operationGridForm.getOperations());

        GuideServiceImpl guideService = new GuideServiceImpl();
        guideService.add(newGuide);
    }

    private void configureGuideElements() {
        H1 guidelabel = new H1("Elementos de Guia");
        guidelabel.addClassName("logo");
        add(guidelabel);

        guideLayout = new HorizontalLayout();
        name = new TextField("Nombre Guia");
        label = new TextField("Label Guia");
        name.setRequired(true);
        name.setErrorMessage("Este campo es obligatorio.");
        label.setRequired(true);
        label.setErrorMessage("Este campo es obligatorio.");
        mainStep = new ComboBox<>("Empezar en:");
        mainStep.setPlaceholder("Id Step Principal");
        mainStep.setRequired(true);
        mainStep.setErrorMessage("Debes seleccionar un Step Principal.");

        guideLayout.add(name,label,mainStep);
    }

    private void configureStepElements() {
        stepGridForm = new StepGridForm();
        stepGridForm.setWidthFull();
        stepGridForm.getStepGrid().asSingleSelect().addValueChangeListener(evt -> updateComboMainStep());
        stepGridForm.getSaveButton().addClickListener(evt -> updateComboMainStep());
        stepSecctionLayout = new HorizontalLayout();
        stepSecctionLayout.setWidthFull();
        stepSecctionLayout.add(stepGridForm);
    }

    private void updateComboMainStep() {
        List<String> mainStepIds = new LinkedList<>();
        for (Step step :stepGridForm.getSteps()){
            mainStepIds.add(step.getTextId());
        }
        mainStep.setItems(mainStepIds);
    }
}