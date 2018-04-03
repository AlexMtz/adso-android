package com.nahtredn.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Me on 13/03/2018.
 */

public class StudiesDone extends RealmObject {
    @PrimaryKey
    private int id;
    @Required
    private String academicLevel;
    private int academicLevelPos;
    @Required
    private String courseName;
    @Required
    private String institute;
    @Required
    private String state;
    private int statePos;
    @Required
    private String startDate;
    @Required
    private String endDate;
    @Required
    private String title;
    private int titlePos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(String academicLevel) {
        this.academicLevel = academicLevel;
    }

    public int getAcademicLevelPos() {
        return academicLevelPos;
    }

    public void setAcademicLevelPos(int academicLevelPos) {
        this.academicLevelPos = academicLevelPos;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getStatePos() {
        return statePos;
    }

    public void setStatePos(int statePos) {
        this.statePos = statePos;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTitlePos() {
        return titlePos;
    }

    public void setTitlePos(int titlePos) {
        this.titlePos = titlePos;
    }
}
