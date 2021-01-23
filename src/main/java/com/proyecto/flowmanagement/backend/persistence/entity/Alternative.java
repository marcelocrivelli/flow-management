package com.proyecto.flowmanagement.backend.persistence.entity;

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
			if(actual.setUnaryCondition(oldCondition,newCondition));
				break;
		}
	}

	public void setBinaryCondition(Condition oldCondition, Condition newCondition)
	{
		for (Condition actual: this.conditions) {
			if(actual.setBinaryCondition(oldCondition,newCondition));
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

}
