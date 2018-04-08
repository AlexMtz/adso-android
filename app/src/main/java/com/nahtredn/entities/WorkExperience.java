package com.nahtredn.entities;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Me on 08/04/2018.
 */

public class WorkExperience extends RealmObject {

    @PrimaryKey
    private int id;
    private String typeExperience;
    private int posTypeExperience;
    private String institute;
    private String jobTitle;
    private Date startJob;
    private Date endJob;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeExperience() {
        return typeExperience;
    }

    public void setTypeExperience(String typeExperience) {
        this.typeExperience = typeExperience;
    }

    public int getPosTypeExperience() {
        return posTypeExperience;
    }

    public void setPosTypeExperience(int posTypeExperience) {
        this.posTypeExperience = posTypeExperience;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Date getStartJob() {
        return startJob;
    }

    public void setStartJob(Date startJob) {
        this.startJob = startJob;
    }

    public Date getEndJob() {
        return endJob;
    }

    public void setEndJob(Date endJob) {
        this.endJob = endJob;
    }
}
