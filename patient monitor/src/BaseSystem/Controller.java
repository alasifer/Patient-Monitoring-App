package BaseSystem;

import BloodPressureController.BloodPressure;
import CholesterolController.Cholesterol;
import BloodPressureController.BloodWindow;
import CholesterolController.CholestWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.json.JSONException;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * class controls how the user see the information gotten from the server
 *
 * @authors: AHMED ABDULWAHID OMAR ALASAIFER & HOW CARL KIT
 * Monash University Malaysia
 * @last_edited: 19/May/2019
 */
public class Controller {

    private Integer clinicianID;
    private Vitals startCholest;
    private Vitals startBloodP;
    private Map<Integer, Patient> clinicianPatients;
    private Map<String, Observer> patList = new Hashtable<>();
    private Thread cholThread;
    private Thread bloodThread;


    // initialize the two obeserver controller: Cholesterol and BloodPressure
    {
        try {
            startCholest = new Cholesterol();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    {
        try{
            startBloodP = new BloodPressure();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    AnchorPane rootPane;
    @FXML
    private TextField clinicianId;
    @FXML
    private TableView patientList;
    @FXML
    private TableColumn patId;
    @FXML
    private TableColumn patName;
    @FXML
    private TableColumn cholest;
    @FXML
    private TableColumn bloodP;
    @FXML
    private TableColumn smoke;

    private ArrayList<Boolean>
            counterCholestM =new ArrayList<>(),
            counterBloodM =new ArrayList<>(),
            counterSmokingM =new ArrayList<>();

    @FXML
    private GridPane CholandBloodM;
    @FXML
    private GridPane ListSmoker;



    /**
     * method that list patients based on the clinician id and display them in a listView
     * along with option to display cholesterol, blood pressure or smoking status
     */
    @FXML
    private void listPatients() throws IOException, JSONException {


        ColumnConstraints c1 = CholandBloodM.getColumnConstraints().get(0);
        c1.setPercentWidth(25);// adjustin the size of the gridpane to display cholesterol and blood pressure data

        clinicianId.setDisable(true);
        clinicianID = Integer.parseInt(clinicianId.getText());

        for (int i = 0; i <4 ; i++) {
            counterCholestM.add(true);
            counterBloodM.add(true);
            counterSmokingM.add(true);
        }

        patId.setCellValueFactory(new PropertyValueFactory<>("patId"));
        patName.setCellValueFactory(new PropertyValueFactory<>("lname"));

        cholest.setCellValueFactory(new PropertyValueFactory<>("C"));
        bloodP.setCellValueFactory(new PropertyValueFactory<>("BP"));
        smoke.setCellValueFactory(new PropertyValueFactory<>("S"));


        clinicianPatients = startCholest.getClinicianPatients(clinicianID);

        ArrayList<Patient> patients = new ArrayList<>();

        for (Map.Entry<Integer, Patient> p : clinicianPatients.entrySet()) {
            p.getValue().getC().setId("C"+p.getValue().getPatId());
            p.getValue().getBP().setId("BP"+p.getValue().getPatId());
            p.getValue().getS().setId("S"+p.getValue().getPatId());

            // setting up method for check box to display respective information for a given patient
            p.getValue().getC().setOnAction(event -> displayCholest(p.getValue()));
            p.getValue().getBP().setOnAction(event -> displayBloodP(p.getValue()));
            p.getValue().getS().setOnAction(event -> displaySmoke(p.getValue()));

            patients.add(p.getValue());
        }
        ObservableList<Patient> items = FXCollections.observableArrayList (
                patients
        );

        patientList.setItems(items);


        // start threads for updating methods in the subject classes Cholesterol and BloodPressure
        cholThread = startThread(startCholest, 1);
        bloodThread = startThread(startBloodP, 1);

    }

    private Thread startThread(Vitals startVital, int mint) {
        Thread thread = new Thread(() ->
        {
            try {
                startVital.updateObservers(mint);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();
        return thread;
    }


    /**
     * allow the user to clean the window and enter another clinician ID
     */
    @FXML
    private void reset(){

        clinicianId.clear();
        patientList.getItems().clear();
        clinicianId.setDisable(false);
        clinicianPatients.clear();

        CholandBloodM.getChildren().clear();
        ListSmoker.getChildren().clear();
        patList.clear();
        cholThread.interrupt();

        bloodThread.interrupt();
        
        // resetting the counter for monitors positions
        for (int i = 0; i <4 ; i++) {
            counterCholestM.set(i, true);
            counterBloodM.set(i, true);
            counterSmokingM.set(i, true);
        }

    }

    /**
     *
     * @param selectedPat Patient object of the patient wanted his/her smoking status to be displayed
     */
    @FXML
    public void displaySmoke(Patient selectedPat){

        int avaPos = monitorCounter(counterSmokingM);
        //checking if user already is displaying four windows
        if (avaPos >= 4){
            JOptionPane.showMessageDialog(null, "Please remove one monitor before adding new one!");
            selectedPat.getS().setSelected(false);
            return;
        }

        TextField smk = new TextField(selectedPat.getSmoking());
        smk.setDisable(true);
        Label patName = new Label(selectedPat.getLname());
        VBox box = new VBox(patName,smk);
        box.setAlignment(Pos.CENTER);
        box.setId("VBoxS"+selectedPat.getPatId().toString());

        ListSmoker.add(box,0,avaPos);

        // setting up check box to remove the smoking monitor for the selectedPat
        selectedPat.getS().setOnAction(event -> removeSmoke(selectedPat, avaPos));
    }

    /**
     * display a patient latest cholesterol value in a text field
     * @param selectedPat Patient object of the patient wanted his/her cholesterol value to be displayed
     */
    @FXML
    public void displayCholest(Patient selectedPat)  {

        CholestWindow newPat = new CholestWindow(selectedPat.getPatId());

        try {
            startCholest.add(newPat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // check if the selectedPat has a cholesterol record, if not display an error message and disable the check box
        if (!newPat.getCheck()){
            showError("cholesterol");
            selectedPat.getC().setDisable(true);
            selectedPat.getC().setSelected(false);
            startCholest.remove(newPat);
            return;
        }
        
        //checking if user already is displaying four windows
        int avaPos = monitorCounter(counterCholestM);
        if (avaPos >= 4){
            JOptionPane.showMessageDialog(null, "Please remove one monitor before adding new one!");
            selectedPat.getC().setSelected(false);
            startCholest.remove(newPat);
            return;
        }

        patList.put("Cholest"+selectedPat.getPatId(), newPat);
        newPat.setVital((Integer) startCholest.vitalData(selectedPat.getPatId()));

        Label patName = new Label(selectedPat.getLname());

        newPat.setCholestVal("Cholesterol Level...." + newPat.getVital().toString());

        newPat.getCholestVal().setId("cholVal" + selectedPat.getPatId().toString());
        newPat.getCholestVal().setDisable(true);

        VBox patData = new VBox(patName, newPat.getCholestVal());
        patData.setSpacing(15);


        patData.setId("VBoxC" + selectedPat.getPatId().toString());

        patData.setAlignment(Pos.CENTER);

        CholandBloodM.add(patData, 0, avaPos);
        
        // setting up check box to remove the cholesterol monitor for the selectedPat
        selectedPat.getC().setOnAction(event -> removeCholest(selectedPat, avaPos));

    }


    /**
     * display blood pressure data (diastolic and systolic) in a line-chart diagram
     * @param selectedPat Patient object of the patient wanted his/her blood pressure data to be displayed
     */
    @FXML
    private void displayBloodP(Patient selectedPat){


        BloodWindow patdata = new BloodWindow(selectedPat.getPatId());

        try {
            startBloodP.add(patdata);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        // check if the selectedPat has a blood pressure record, if not display an error message and disable the check box

        if (!patdata.getChck()){
            selectedPat.getBP().setDisable(true);
            showError("Blood Pressure");
            selectedPat.getBP().setSelected(false);
            startBloodP.remove(patdata);
            return;
        }
        //checking if user already is displaying four windows

        int avaPos = monitorCounter(counterBloodM);
        if (avaPos >= 4){
            JOptionPane.showMessageDialog(null, "Please remove one monitor before adding new one!");
            selectedPat.getBP().setSelected(false);
            startBloodP.remove(patdata);
            return;
        }

        patList.put("Blood"+selectedPat.getPatId(), patdata);

        patdata.setVital((LinkedHashMap<Integer, ArrayList<Integer>>) startBloodP.vitalData(selectedPat.getPatId()));
        ArrayList<ArrayList<Integer>> bloodData = new ArrayList<>();
        ArrayList<Integer> timeAxis = new ArrayList<>();

        for (Map.Entry<Integer, ArrayList<Integer>> b : patdata.getVital().entrySet()) {
            bloodData.add(b.getValue());
            timeAxis.add(b.getKey());
        }

        for (int i = 0; i < bloodData.size(); i++) {
            patdata.getSystolic().getData().add(new XYChart.Data(timeAxis.get(i), bloodData.get(i).get(0)));
            patdata.getDiastolic().getData().add(new XYChart.Data(timeAxis.get(i), bloodData.get(i).get(1)));
        }

        patdata.getxAxis().setLowerBound(timeAxis.get(0)-1);
        patdata.getxAxis().setUpperBound(timeAxis.get(timeAxis.size()-1)+1);

        patdata.getLineChart().setTitle(selectedPat.getLname());
        patdata.getLineChart().getData().addAll(patdata.getSystolic(), patdata.getDiastolic());

        VBox box = new VBox(patdata.getLineChart());
        box.setId("VBoxB" + selectedPat.getPatId());

        CholandBloodM.add(box, 1, avaPos);

        // setting up check box to remove the blood pressure monitor for the selectedPat

        selectedPat.getBP().setOnAction(event -> removeBloodP(selectedPat, avaPos));

    }

    /**
     * to keep track of available position for each monitor
     * @param counter list of length of maximum number of monitor can be displayed
     * @return index of available position
     */
    private int monitorCounter(ArrayList<Boolean> counter) {
        int avaPos = 0;
        for (Boolean s : counter) {
            if (s){
                counter.set(avaPos, false);
                return avaPos;
            }
            else{
                avaPos += 1;
            }
        }
        return avaPos;
    }

    /**
     * remove the monitor from being displayed when the checkbox is unchecked for cholesterol
     * @param selectedPat patient object of which patient to be removed
     * @param pos position at the monitor panel
     */
    @FXML
    public void removeCholest(Patient selectedPat, int pos){
        selectedPat.getC().setOnAction(event -> displayCholest(selectedPat));
        counterCholestM.set(pos, true);
        VBox r = (VBox) rootPane.lookup("#VBoxC"+selectedPat.getPatId());
        CholandBloodM.getChildren().remove(r);

        startCholest.remove(patList.get("Cholest"+selectedPat.getPatId()));
    }

    /**
     * remove the monitor from being displayed when the checkbox is unchecked for blood pressure
     * @param selectedPat patient object of which patient to be removed
     * @param pos position at the monitor panel
     */
    @FXML
    public void removeBloodP(Patient selectedPat, int pos)
    {
        selectedPat.getBP().setOnAction(event -> displayBloodP(selectedPat));
        counterBloodM.set(pos, true);
        VBox r = (VBox) rootPane.lookup("#VBoxB"+selectedPat.getPatId());
        CholandBloodM.getChildren().remove(r);

        startBloodP.remove(patList.get("Blood"+selectedPat.getPatId()));
    }


    /**
     * remove the monitor from being displayed when the checkbox is unchecked for smoking status
     * @param selectedPat patient object of which patient to be removed
     * @param pos position at the monitor panel
     */
    public void removeSmoke(Patient selectedPat, int pos)
    {
        selectedPat.getS().setOnAction(event -> displaySmoke(selectedPat));
        counterSmokingM.set(pos, true);
        VBox r = (VBox) rootPane.lookup("#VBoxS"+selectedPat.getPatId());
        ListSmoker.getChildren().remove(r);
    }

    /**
     * show an alert message if the patient doesn't have the selected type of data
     * @param ErrorType string that represent what data is not available
     */
    public void showError(String ErrorType)
    {
        String failed = "No "+ErrorType+" Data for this patient";
        JOptionPane.showMessageDialog(null, failed);
    }

}
