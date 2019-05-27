package BloodPressureController;


import BaseSystem.Observer;
import BaseSystem.Vitals;
import BaseSystem.ListVitals;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class is responsible to keep track of blood pressure windows added and update them accordingly
 *
 * @authors: AHMED ABDULWAHID OMAR ALASAIFER & HOW CARL KIT
 * Monash University Malaysia
 * @last_edited: 17/May/2019
 */

public class BloodPressure extends Vitals {


    private Map<Observer, LocalDateTime> bloodWindow = new Hashtable<>();
    private Map<Integer, LinkedHashMap<Integer, ArrayList<Integer>>> patientBloodP = new Hashtable<>();


    /**
     * add a new window to the list and store the time it was created. And check whether this given patient has
     * blood pressure data or not
     *
     * @param obs
     */
    @Override
    public void add(Observer obs){
        //checking if a given patient has cholesterol record and setting the boolean value accordingly
        if (patBloodP(obs.getPatientId()).size() == 0){
            obs.setCheck(false);
        }
        else {
            obs.setCheck(true);
            this.bloodWindow.put(obs, LocalDateTime.now());//adding the new observer to the hashmap along with the time it was initialised 
        }
    }

    /**
     * remove blood pressure window from the updating list
     * @param e
     */
    @Override
    public void remove(Observer e) {
        this.bloodWindow.remove(e);
    }

    /**
     * update each window based on the number of mint needed
     * @param mint time in minutes
     * @throws IOException
     */
    @Override
    protected void updateObservers(int mint) throws IOException {
        while(true) {
            //using synchronized to avoid any error that might occur when observer is being removed from the hashmap
            synchronized (bloodWindow) {
                for (Map.Entry<Observer, LocalDateTime> obs : bloodWindow.entrySet()) {
                    Duration period = Duration.between(LocalDateTime.now(), obs.getValue());

                    if (Math.abs(period.getSeconds()) > (mint * 60)) {

                        int patientid = obs.getKey().getPatientId();
                        LinkedHashMap<Integer, ArrayList<Integer>> patb = patBloodP(patientid);
                        obs.getKey().setVital(patb);
                        obs.getKey().update();
                        obs.setValue(LocalDateTime.now());
                    }
                }
            }
        }

    }


    /**
     * get the blood pressure values (diastolic and systolic) along withe the date recorded and store them
     * in linkedhashmap to preserve the order
     *
     * @param patId
     * @return linkedhashmap with the data or empty if no data is available for the given patient id
     */
    public LinkedHashMap<Integer, ArrayList<Integer>> patBloodP (Integer patId)  {
        LinkedHashMap<Integer, ArrayList<Integer>> patObs = new LinkedHashMap<>();
        try {
            JSONArray entries = super.patientObs(patId, ListVitals.BLOOD_PRESSURE);


            for (int i = 0; i < entries.length(); i++) {
                JSONObject getVal = entries.getJSONObject(i).getJSONObject("resource");
                JSONArray data = getVal.getJSONArray("component");

                Integer diastolic = data.getJSONObject(0).getJSONObject("valueQuantity").getInt("value");
                Integer systolic = data.getJSONObject(1).getJSONObject("valueQuantity").getInt("value");
                String date = getVal.getString("effectiveDateTime");


                ArrayList<Integer> bloodPRead = new ArrayList<>();
                bloodPRead.add(diastolic);
                bloodPRead.add(systolic);

                patObs.put(Integer.parseInt(date.substring(0,4)), bloodPRead);

            }
            patientBloodP.put(patId, patObs);
            return patObs;

        }catch (Exception e){
            return patObs;
        }

    }

    /**
     * get a given patient data that has been retrieved earlier
     *
     * @param patID
     * @return linkedhashmap that contain patient blood pressure data
     */
    public LinkedHashMap<Integer, ArrayList<Integer>> vitalData(int patID) {
        return this.patientBloodP.get(patID);
    }
}

