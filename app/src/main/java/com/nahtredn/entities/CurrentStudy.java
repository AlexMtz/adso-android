package com.nahtredn.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Me on 14/03/2018.
 */

public class CurrentStudy extends RealmObject {
    @PrimaryKey
    private int id;
    private String institute;
    private String startTime;
    private String endTime;
    private String days;
    private String academicLevel;
    private int positionAcademicLevel;
    private String courseName;
    private String grade;
    private int positionGrade;
    private String modality;
    private int positionModality;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(String academicLevel) {
        this.academicLevel = academicLevel;
    }

    public int getPositionAcademicLevel() {
        return positionAcademicLevel;
    }

    public void setPositionAcademicLevel(int positionAcademicLevel) {
        this.positionAcademicLevel = positionAcademicLevel;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getPositionGrade() {
        return positionGrade;
    }

    public void setPositionGrade(int positionGrade) {
        this.positionGrade = positionGrade;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public int getPositionModality() {
        return positionModality;
    }

    public void setPositionModality(int positionModality) {
        this.positionModality = positionModality;
    }

    public String getSchedule(){
        return this.getStartTime() + " - " + this.getEndTime();
    }
}
