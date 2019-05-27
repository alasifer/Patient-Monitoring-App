package BaseSystem;

/**
 * enum to store the list of code of different vitals
 *
 * @authors: AHMED ABDULWAHID OMAR ALASAIFER & HOW CARL KIT
 * Monash University Malaysia
 * @last_edited: 14/May/2019
 */
public enum ListVitals {

    Total_Cholesterol("2093-3"),
    BLOOD_PRESSURE("55284-4"),
    Smoking("72166-2");

    private String vital;
    ListVitals(String vital) {
        this.vital = vital;
    }

    public String getVital() {
        return vital;
    }
}
