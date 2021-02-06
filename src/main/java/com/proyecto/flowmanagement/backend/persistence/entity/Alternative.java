package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.commun.ValidationDTO;
import com.proyecto.flowmanagement.backend.def.TypeOperation;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "alternative")
public class Alternative  extends AbstractEntity{
	
	@Column(name = "guide_name")
	private String guideName;
	
	@Column(name = "label")
	private String label;

	@Column(name = "next_step")
	private String nextStep;

	@Column(name = "newStep")
	private boolean newStep;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="alternative_unary_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Condition> conditions;

	public String getGuideName() {
		return guideName;
	}

	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}

	public String getNextStep() {
		return nextStep;
	}

	public void setNextStep(String nextStep) {
		this.nextStep = nextStep;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	public void addCondition(Condition condition)
	{
		if(this.getConditions() == null)
			this.conditions = new LinkedList<>();

		this.conditions.add(condition);
	}


	public void setUnaryCondition(Condition oldCondition, Condition newCondition)
	{
		for (Condition actual: this.conditions) {
			if(actual == oldCondition)
			{
				actual.setOperation(newCondition.getOperation());
				int index = this.conditions.indexOf(actual);
				this.conditions.set(index, actual);
			}
			else if(actual.setUnaryCondition(oldCondition,newCondition));
				break;
		}
	}

	public void setBinaryCondition(Condition oldCondition, Condition newCondition)
	{
		for (Condition actual : this.conditions) {
			if(actual == oldCondition)
			{
				int index = this.conditions.indexOf(actual);
				actual.setOperation(newCondition.getOperation());
				this.conditions.set(index, actual);
			}
			else if(actual.setBinaryCondition(oldCondition,newCondition));
				break;
		}
	}

	public void deleteCondition(Condition eliminar)
	{
		for (Condition actual: this.conditions) {
			if(actual == eliminar)
				conditions.remove(actual);
			else if(actual.deleteCondition(eliminar));
			break;
		}
	}

	public ValidationDTO validarAltarnative()
	{
		ValidationDTO validationGuide = new ValidationDTO();
		validationGuide.setLabel("Alt-Label: " + this.getLabel());

		if(this.label.trim().isEmpty())
			validationGuide.addError("El campo Label es obligatorio");

		if(this.getNextStep().trim().isEmpty() && this.nextStep.trim().isEmpty())
			validationGuide.addError("Debe seleccionar una guia o un step para el Alternative");

		if(this.conditions != null && this.conditions.size() > 0)
		{
			for (Condition condition: this.conditions) {
				if(condition.getType() == TypeOperation.unaryCondition)
				{
					ValidationDTO validacionUnary = condition.validarUnaryCompleto();
					if(validacionUnary.getError().size() > 0 || validacionUnary.getValidationDTOList().size() > 0)
						validationGuide.addList(condition.validarUnaryCompleto());
				}
				else
				{
					ValidationDTO validacionBinary = condition.validarBinaryCompleto();
					if(validacionBinary.getError().size() > 0 || validacionBinary.getValidationDTOList().size() >0)
						validationGuide.addList(condition.validarBinaryCompleto());
				}
			}
		}
		return validationGuide;
	}

	public String validacionIncompleta()
	{
		String retorno = "";

		if(this.label.trim().isEmpty())
			retorno = "El campo Label es obligatorio";

		return retorno;
	}

	public boolean isNewStep() {
		return newStep;
	}

	public void setNewStep(boolean newStep) {
		this.newStep = newStep;
	}

	public boolean getNewStep() {
		return this.newStep;
	}
}
