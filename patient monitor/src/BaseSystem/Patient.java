package BaseSystem;

import javafx.scene.control.CheckBox;

/**
 * this class contain basic patient information and their smoking status
 *
 * @authors: AHMED ABDULWAHID OMAR ALASAIFER & HOW CARL KIT
 * Monash University Malaysia
 * @last_edited: 12/May/2019
 */

public class Patient {


    private Integer patId;
    private String fname;
    private String lname;
    private String smoking;
    private CheckBox C = new CheckBox();//check box for displaying cholesterol data
    private CheckBox BP = new CheckBox();//check box for displaying blood pressure data
    private CheckBox S = new CheckBox();//check box for displaying smoking data

    public Patient(Integer patid, String fname, String lname){
        this.patId = patid;
        this.fname = fname;
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public String getSmoking() {
        return smoking;
    }

    public String getLname() {
        return lname;
    }

    public Integer getPatId() {
        return patId;
    }

    public void setC(CheckBox c){
        this.C = c;
    }

    public void setBP(CheckBox BP) {
        this.BP = BP;
    }

    public void setS(CheckBox s) {
        S = s;
    }

    public CheckBox getBP() {
        return BP;
    }

    public CheckBox getS() {
        return S;
    }

    public CheckBox getC() {
        return C;
    }
}
