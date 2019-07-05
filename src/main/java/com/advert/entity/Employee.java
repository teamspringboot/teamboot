package com.advert.entity;

import java.math.BigDecimal;

import com.advert.util.entity.AbstractEntity;

public class Employee extends AbstractEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String classes;

    private String office;

    private BigDecimal salary;


    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes == null ? null : classes.trim();
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office == null ? null : office.trim();
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}