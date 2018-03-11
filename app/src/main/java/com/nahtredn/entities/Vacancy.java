package com.nahtredn.entities;

import java.util.Date;

/**
 * Created by Me on 10/03/2018.
 */

public class Vacancy {
    private String id;
    private String name;
    private String companyName;
    private String companyId;
    private String salary;
    private Address address;
    private String status;
    private Date added;
    private String tags[];
    private String benefits;
    private String knowledgement[];
    private String skills[];
    private String schedule;
    private String responsabilities[];
    private String description;

    public Vacancy(String name, String companyName, String salary) {
        this.name = name;
        this.companyName = companyName;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public String[] getKnowledgement() {
        return knowledgement;
    }

    public void setKnowledgement(String[] knowledgement) {
        this.knowledgement = knowledgement;
    }

    public String[] getSkills() {
        return skills;
    }

    public void setSkills(String[] skills) {
        this.skills = skills;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String[] getResponsabilities() {
        return responsabilities;
    }

    public void setResponsabilities(String[] responsabilities) {
        this.responsabilities = responsabilities;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
