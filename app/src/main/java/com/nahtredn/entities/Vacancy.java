package com.nahtredn.entities;

import java.util.Date;

/**
 * Created by Me on 10/03/2018.
 */

public class Vacancy {
    private String id;
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String rhEmail;
    private String rhPhone;
    private String street;
    private String colony;
    private String numExt;
    private String municipality;
    private String state;
    private String jobTitle;
    private String hoursWork;
    private String description;
    private String benefits;
    private String skills;
    private String knowledges;
    private String salary;

    public Vacancy() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getRhEmail() {
        return rhEmail;
    }

    public void setRhEmail(String rhEmail) {
        this.rhEmail = rhEmail;
    }

    public String getRhPhone() {
        return rhPhone;
    }

    public void setRhPhone(String rhPhone) {
        this.rhPhone = rhPhone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getColony() {
        return colony;
    }

    public void setColony(String colony) {
        this.colony = colony;
    }

    public String getNumExt() {
        return numExt;
    }

    public void setNumExt(String numExt) {
        this.numExt = numExt;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getHoursWork() {
        return hoursWork;
    }

    public void setHoursWork(String hoursWork) {
        this.hoursWork = hoursWork;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(String knowledges) {
        this.knowledges = knowledges;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getPlace(){
        return municipality + ", " + state;
    }
}
