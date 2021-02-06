package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.commun.ValidationDTO;
import com.proyecto.flowmanagement.backend.def.OperationType;
import com.proyecto.flowmanagement.backend.def.SimpleOperationType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "simple_operation")
public class SimpleOperation extends Operation{

    @Column(name = "type")
    private SimpleOperationType type;

    @Column(name = "service")
    private String service;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "operation_id")
    @Fetch(FetchMode.SUBSELECT)
    private List<Groups> groups;

    public SimpleOperationType getType() {
        return type;
    }

    public void setType(SimpleOperationType type) {
        this.type = type;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public ValidationDTO validateOperation() {

        ValidationDTO validationGuide = new ValidationDTO();
        validationGuide.setLabel("Op-Label: " + getLabel() );

        if(this.getName().isEmpty())
            validationGuide.addError("El campo Name es obligatorio");

        if (this.getLabel().isEmpty())
            validationGuide.addError("El campo Label es obligatorio");

        if (this.getOperationType().toString() == null)
            validationGuide.addError("El campo OperationType es obligatorio");

        if (this.getOperationOrder() != null){
            if (this.getOperationOrder() <= 0)
                validationGuide.addError("El campo OperationOrder debe ser un entero positivo");
        }

        if (this.getNotifyOperationDelay() != null){
            if (this.getNotifyOperationDelay() <= 0)
                validationGuide.addError("El campo NotifyOperationDelay debe ser un entero positivo");
        }

        if (this.type.toString().isEmpty())
            validationGuide.addError("El campo Type es obligatorio");

        if (this.service.isEmpty())
            validationGuide.addError("El campo service es obligatorio");

        return validationGuide;
    }

    public String incompleteValidation(){

        if (this.getName().isEmpty())
            return "El campo Name es obligatorio";

        if (this.getLabel().isEmpty())
            return "El campo Label es obligatorio";

        if (this.getOperationType() == null)
            return "El campo OperationType es obligatorio";

//        if (this.getOperationOrder() != null){
//            if (this.getOperationOrder() <= 0)
//                return "El campo OperationOrder debe ser un entero positivo";
//        }

        if (this.getNotifyOperationDelay() != null){
            if (this.getNotifyOperationDelay() <= 0)
                return "El campo NotifyOperationDelay debe ser un entero positivo";
        }

        if (this.type == null)
            return "El campo Type es obligatorio";

        if (this.service.isEmpty())
            return "El campo service es obligatorio";

        return "";
    }

    public List<Groups> getGroups() {
        return groups;
    }

    public void setGroups(List<Groups> groups) {
        this.groups = groups;
    }
}
