package BloodPressureController;

import BaseSystem.Observer;
import javafx.application.Platform;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javax.swing.*;
import java.util.*;


/**
 * this class implements the observer Interface and it is responsible to display patient blood pressure in graph and update it as needed
 *
 * @authors: AHMED ABDULWAHID OMAR ALASAIFER & HOW CARL KIT
 * Monash University Malaysia
 * @last_edited: 17/May/2019
 * */
public class BloodWindow implements Observer {


    private LinkedHashMap<Integer, ArrayList<Integer>> bloodPData =  new LinkedHashMap<>();
    private Integer patientId;
    private Boolean chck;
    private NumberAxis xAxis = new NumberAxis();
    private NumberAxis yAxis = new NumberAxis();
    private LineChart lineChart = new LineChart(xAxis,yAxis);
    private final Integer sysAlertVal = 180;
    private final Integer daiAlertVal = 120;
    private XYChart.Series systolic = new XYChart.Series();
    private XYChart.Series diastolic = new XYChart.Series();


    /**
     * @param id patient ID
     */
    public BloodWindow(Integer id){

        this.patientId = id;
        systolic.setName("Systolic");
        diastolic.setName("Diastolic");
        xAxis.setLabel("Time");
        yAxis.setLabel("Blood Pressure");
        lineChart.setId("bloodVal"+id.toString());
        lineChart.setLegendSide(Side.LEFT);
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit(1);

    }

    /**
     * updating is done by removing the first values and appending lateast value to the end of the graph
     */
    @Override
    public void update() {


        systolic.getData().remove(0);
        diastolic.getData().remove(0);

        Set<Map.Entry<Integer, ArrayList<Integer>>> mapSet = bloodPData.entrySet();

        Map.Entry<Integer, ArrayList<Integer>> lastVal = (Map.Entry<Integer, ArrayList<Integer>>) mapSet.toArray()[bloodPData.size()-1];


        Platform.runLater(()->{
            systolic.getData().add( new XYChart.Data(lastVal.getKey(), lastVal.getValue().get(0)));
            diastolic.getData().add( new XYChart.Data(lastVal.getKey(), lastVal.getValue().get(1)));
        });


        Set<Integer> getDate = bloodPData.keySet();

        xAxis.setUpperBound(lastVal.getKey()+1);
        xAxis.setLowerBound((Integer) getDate.toArray()[0]);

        //checking if any Hypertensive crisis occured 
        if (lastVal.getValue().get(0) > sysAlertVal){
            JOptionPane.showMessageDialog(null, "Hypertensive crisis for " + patientId.toString());
        }
        if (lastVal.getValue().get(1) > daiAlertVal){
            JOptionPane.showMessageDialog(null, "Hypertensive crisis for "+patientId.toString());

        }


    }

    public LinkedHashMap<Integer, ArrayList<Integer>> getVital(){

        return this.bloodPData;
    }

    @Override
    public Integer getPatientId() {

        return patientId;
    }

    @Override
    public void setVital(Integer Vital) { }

    @Override
    public void setVital(LinkedHashMap<Integer, ArrayList<Integer>> bloodp) {
        this.bloodPData = bloodp;
    }

    @Override
    public void setCheck(Boolean chck) {
        this.chck = chck;
    }


    public Boolean getChck(){
        return chck;
    }


    public NumberAxis getxAxis() {
        return xAxis;
    }


    public NumberAxis getyAxis() {
        return yAxis;
    }


    public LineChart getLineChart() {
        return lineChart;
    }


    public XYChart.Series getSystolic() {
        return this.systolic;
    }


    public XYChart.Series getDiastolic() {
        return this.diastolic;
    }

}
