package CholesterolController;


import BaseSystem.Observer;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;


/**
 * this class implements the observer Interface and it is responsible to display patient cholesterol level in text and update it as needed
 *
 * @authors: AHMED ABDULWAHID OMAR ALASAIFER & HOW CARL KIT
 * Monash University Malaysia
 * @last_edited: 12/May/2019
 */
public class CholestWindow implements Observer {

    private Integer vital;
    private Integer patientId;
    private TextField cholestVal = new TextField();
    private Boolean chck;


    /**
     * @param id patient ID
     */
    public CholestWindow(Integer id){
        this.patientId = id;
    }

    /**
     * this method is updates the cholesterol value on the interface every time it is called
     * @throws IOException
     */
    @Override
    public void update() {
        this.setCholestVal("Cholesterol Level...." + this.getVital().toString());
    }

    @Override
    public Integer getPatientId() {
        return patientId;
    }

    @Override
    public void setVital(Integer Vital) {
        this.vital = Vital;
    }

    @Override
    public void setVital(LinkedHashMap<Integer, ArrayList<Integer>> Vital) { }

    @Override
    public void setCheck(Boolean chck) {
        this.chck = chck;
    }

    public Boolean getCheck() {
        return this.chck;
    }

    public Integer getVital() {
        return vital;
    }

    public TextField getCholestVal() {
        return cholestVal;
    }

    public void setCholestVal(String chol) {
        this.cholestVal.setText(chol);
    }
}
