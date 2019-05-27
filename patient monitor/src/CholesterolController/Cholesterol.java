package CholesterolController;

import BaseSystem.Observer;
import BaseSystem.Vitals;
import BaseSystem.ListVitals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * This class is responsible to keep track of cholesterol windows added and update them accordingly
 *
 * @authors: AHMED ABDULWAHID OMAR ALASAIFER & HOW CARL KIT
 * Monash University Malaysia
 * @last_edited: 12/May/2019
 */
public class Cholesterol extends Vitals {

    private Map<Observer, LocalDateTime> windows = new Hashtable<>();
    private Map<Integer, Integer> patientCholest = new Hashtable<>();

    /**
     * this method adds each new observer to a hashmap along with the time this window has been created
     * @param obs
     */
    @Override
    public void add(Observer obs){
        //checking if a given patient has blood pressure record and setting the boolean value accordingly
        if (patCholest(obs.getPatientId()).size() == 0){
            obs.setCheck(false);

        }else{
            obs.setCheck(true);
            this.windows.put(obs, LocalDateTime.now());//adding the new observer to the hashmap along with the time it was initialised 
        }
    }

    /**
     * remove the observer <e> observer from the hashmap
     * @param e
     */
    @Override
    public void remove(Observer e) {
        this.windows.remove(e);
    }

    /**
     * this method keep iterating over the list of observer in the hashmap and check if last update was made more than one hour ago
     *
     * @throws IOException
     * @throws JSONException
     */
    @Override
    protected void updateObservers(int mint) throws IOException {

        while(true) {
            //using synchronized to avoid any error that might occur when observer is being removed from the hashmap

            synchronized (windows) {
                for (Map.Entry<Observer, LocalDateTime> obs : windows.entrySet()) {
                    Duration period = Duration.between(LocalDateTime.now(), obs.getValue());

                    if (Math.abs(period.getSeconds()) > (mint * 60)) {
                        int patientid = obs.getKey().getPatientId();
                        ArrayList<Integer> patCh = patCholest(patientid);
                        obs.getKey().setVital(patCh.get(patCh.size() - 1));
                        obs.getKey().update();
                        obs.setValue(LocalDateTime.now());
                    }
                }
            }
        }
    }


    public ArrayList<Integer> patCholest(Integer patID) {
        ArrayList<Integer> patObs = new ArrayList<>();
        try {
            JSONArray entries = super.patientObs(patID, ListVitals.Total_Cholesterol);

            for (int i = 0; i < entries.length(); i++) {
                JSONObject getVal = entries.getJSONObject(i).getJSONObject("resource");

                Integer s = getVal.getJSONObject("valueQuantity").getInt("value");
                patObs.add(s);
            }
            patientCholest.put(patID, patObs.get(patObs.size() - 1));
            return patObs;

        }catch (Exception e){
            return patObs;
        }
    }


    /**
     * given a patient id, this method returns the last cholesterol value
     * @param patID
     * @return cholesterol value
     */
    public Integer vitalData(int patID){
        return this.patientCholest.get(patID);
    }
}


