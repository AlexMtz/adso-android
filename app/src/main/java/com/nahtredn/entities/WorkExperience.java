package com.nahtredn.entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    public String getDates(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        return dateFormat.format(startJob) + " - " + dateFormat.format(endJob);
    }

    public String getDuration(){
        long diffInMillies = endJob.getTime() - startJob.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(diffInMillies);
        return convertDays((int) days);
    }

    private String convertDays(int days){
        StringBuffer st = new StringBuffer();
        int years = days / 360;
        if (years > 0) st.append(years).append(" años, ");
        days = days - (years * 360);
        int month = days / 30;
        if (month > 0) st.append(month).append(" meses ");
        return st.toString();
    }
}
