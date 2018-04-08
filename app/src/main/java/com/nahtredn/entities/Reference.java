package com.nahtredn.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Me on 08/04/2018.
 */

public class Reference extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;
    private String jobTitle;
    private String timeMeet;
    private String typeTimeMeet;
    private int posTypeTimeMeet;
    private String phone;
    private String state;
    private int posState;
    private String municipality;
    private int posMunicipality;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getTimeMeet() {
        return timeMeet;
    }

    public void setTimeMeet(String timeMeet) {
        this.timeMeet = timeMeet;
    }

    public String getTypeTimeMeet() {
        return typeTimeMeet;
    }

    public void setTypeTimeMeet(String typeTimeMeet) {
        this.typeTimeMeet = typeTimeMeet;
    }

    public int getPosTypeTimeMeet() {
        return posTypeTimeMeet;
    }

    public void setPosTypeTimeMeet(int posTypeTimeMeet) {
        this.posTypeTimeMeet = posTypeTimeMeet;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPosState() {
        return posState;
    }

    public void setPosState(int posState) {
        this.posState = posState;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public int getPosMunicipality() {
        return posMunicipality;
    }

    public void setPosMunicipality(int posMunicipality) {
        this.posMunicipality = posMunicipality;
    }

    public String getTimeToMeet(){
        return timeMeet + " " + typeTimeMeet;
    }
}
