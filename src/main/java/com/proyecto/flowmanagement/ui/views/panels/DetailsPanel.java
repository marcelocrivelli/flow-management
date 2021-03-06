package com.proyecto.flowmanagement.ui.views.panels;

import com.proyecto.flowmanagement.backend.commun.CustomNode;
import com.proyecto.flowmanagement.backend.commun.TestDTO;
import com.proyecto.flowmanagement.backend.def.TypeOperation;
import com.proyecto.flowmanagement.backend.persistence.entity.*;
import com.proyecto.flowmanagement.ui.views.grids.StepGridForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;

import java.util.List;

@CssImport("./styles/details-panel-form.css")
public class DetailsPanel  extends HorizontalLayout {

    HorizontalLayout stepSecctionLayout = new HorizontalLayout();
    Accordion accordion = new Accordion();
    FormLayout basicInformation = new FormLayout();
    TreeGrid<CustomNode> arbol = new TreeGrid<CustomNode>(CustomNode.class);
    TreeDataProvider<CustomNode> dataProvider = (TreeDataProvider<CustomNode>) arbol.getDataProvider();
    TreeData<CustomNode> data = dataProvider.getTreeData();

    public DetailsPanel()
    {
        arbol.removeAllColumns();
        arbol.addColumn("text");
        arbol.addComponentColumn(this::getFoo).setHeader(new Span("Header Component")).setId("someID");
        arbol.setHierarchyColumn("text");

        stepSecctionLayout.add(arbol);
        basicInformation.setWidthFull();
        stepSecctionLayout.setWidthFull();
//        stepSecctionLayout.setId("step-Layout");
        stepSecctionLayout.setClassName("step-layout");
//        stepSecctionLayout.setClassName("details-layout");
        setWidthFull();
        basicInformation.add(stepSecctionLayout);
        basicInformation.setClassName("details-layout");
        accordion.add("Resumen de Guia", basicInformation);
        accordion.close();
        add(accordion);
    }

    public Component getFoo(CustomNode s) {
        Div div = new Div();
        div.setText("Nombre: " + s.toString());
        return div;
    }

    public void updateGridDetails(Guide first)
    {
        data.clear();
        arbol.getDataProvider().refreshAll();

        agregarGuia(null,first);

        arbol.getDataProvider().refreshAll();
    }

    private void agregarGuia(CustomNode padre, Guide aux) {

        CustomNode guia = new CustomNode("GuideName: " + aux.getName());

        data.addItem(padre,guia);

        if(aux.getSteps() != null)
        {
            for (Step step: aux.getSteps()) {
                CustomNode stepAux = new CustomNode("StepLabel: " +step.getLabel());
                data.addItem(guia, stepAux);
                agregarAlternativs(step, stepAux);
            }
        }
        if(aux.getGuides() != null)
        {
            for (Guide temp : aux.getGuides()) {
                agregarGuia(guia, temp);
            }
        }
    }

    private void agregarAlternativs(Step step,CustomNode stepAux) {
        if(step.getAlternatives() != null)
        {
            for (Alternative alternative: step.getAlternatives() ) {
                CustomNode alternativeNode = new CustomNode("AlternativeLabel: " +alternative.getLabel());
                data.addItem(stepAux, alternativeNode);

                if(alternative.getConditions() !=null)
                {
                    for (Condition condition : alternative.getConditions()) {
                        agregarCondition(alternativeNode, condition);
                    }
                }
            }
        }
    }

    private void agregarCondition(CustomNode alternativeNode,Condition condition)
    {
        CustomNode conditionNode = new CustomNode(condition.getOperation());
        data.addItem(alternativeNode,conditionNode);

        if(condition.getHijoIzquierdo() != null)
            agregarCondition(conditionNode, condition.getHijoIzquierdo());
        if(condition.getHijoDerecho() != null)
            agregarCondition(conditionNode, condition.getHijoDerecho());
    }
}
