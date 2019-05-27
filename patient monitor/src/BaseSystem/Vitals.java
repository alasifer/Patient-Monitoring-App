package BaseSystem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Hashtable;

/**
* This class responsible to provide abstract methods for its children classes as well as retrieve basic information about practitioners and patients
*
* @authors: AHMED ABDULWAHID OMAR ALASAIFER & HOW CARL KIT
* Monash University Malaysia
* @last_edited: 14/May/2019
* */
public abstract class Vitals {


    private Hashtable<Integer, Patient> clinicPatients = new Hashtable<>();



    public abstract void add(Observer obs) throws IOException, JSONException;

    public abstract void remove(Observer e);

    protected abstract void updateObservers(int mint) throws IOException, JSONException, InterruptedException;

    public abstract Object vitalData(int patID);


    /**
     * this return all the values of a given vital needed in a form of JSONARRAY
     * @param patId -> patient ID;
     *        vital  -> vital code from the enum ListVitalss
     * @return -> JSONARRAY
     *
    * */

    public JSONArray patientObs (Integer patId, ListVitals vital) throws IOException, JSONException {
        Detail data = new Detail("Observation", patId, null);
        JSONObject obs = data.filter(vital.getVital());

        return obs.getJSONArray("entry");
    }


    /**
     * get list of patient under given clinician
     *
     * @param clinicianID
     * @return hashtable that has patient id as a key and patient object as a value
     * */

    public Hashtable<Integer, Patient> getClinicianPatients (Integer clinicianID) throws IOException, JSONException {
        Detail data = new Detail("Encounter?practitioner=Practitioner", clinicianID, "&_count=50");
        JSONObject encounterJson = data.getDetail();
        JSONArray entries = encounterJson.getJSONArray("entry");

        for (int i = 0; i < entries.length(); i++) {

            String p = entries.getJSONObject(i).getJSONObject("resource").getJSONObject("subject").getString("reference");
            int slash = p.indexOf("/");
            Integer PatientID = Integer.parseInt(p.substring(slash + 1));

            Patient pat = patData(PatientID);
            if (!(clinicPatients.containsKey(PatientID))){
                clinicPatients.put(PatientID,pat);
            }
        }
        return clinicPatients;
    }

    /**
     * get patient name and smoking status
     *
     * @param patID: patient ID
     * @return Patient object
     *
     **/

    public Patient patData(Integer patID) throws IOException, JSONException {
        Detail patData = new Detail("Patient", patID, null);
        JSONObject patObj = patData.getDetail();
        JSONObject name = patObj.getJSONArray("name").getJSONObject(0);

        Patient pat = new Patient(patID, name.getJSONArray("given").getString(0),name.getString("family"));

        Detail smokeData = new Detail("Observation", patID, null);
        JSONObject smokeJS = smokeData.filter(ListVitals.Smoking.getVital());

        String smokeStat = smokeJS.getJSONArray("entry").getJSONObject(0).getJSONObject("resource")
                .getJSONObject("valueCodeableConcept").getString("text");
        pat.setSmoking(smokeStat);
        return pat;
    }

}
