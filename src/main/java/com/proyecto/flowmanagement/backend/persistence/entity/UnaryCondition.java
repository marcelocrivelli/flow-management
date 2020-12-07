package com.proyecto.flowmanagement.backend.persistence.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@Table(name = "unary_condition")
public class UnaryCondition  extends AbstractEntity{

    @Column(name = "operation_name")
    private String OperationName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="unary_condition_id")
    @Fetch(FetchMode.SELECT)
    ConditionParameter conditionParameter;

    public String getOperationName() {
        return OperationName;
    }

    public void setOperationName(String operationName) {
        OperationName = operationName;
    }

    public ConditionParameter getConditionParameter() {
        return conditionParameter;
    }

    public void setConditionParameter(ConditionParameter conditionParameter) {
        this.conditionParameter = conditionParameter;
    }
}