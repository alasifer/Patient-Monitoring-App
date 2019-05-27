package BaseSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * this Interface defines the methods that each observer needs to have
 *
 * @authors: AHMED ABDULWAHID OMAR ALASAIFER & HOW CARL KIT
 * Monash University Malaysia
 * @last_edited: 12/May/2019
 */
public interface Observer {

    void update() throws IOException;

    Integer getPatientId();

    void setVital(Integer Vital);

    void setVital(LinkedHashMap<Integer, ArrayList<Integer>> bloodp);
    void setCheck(Boolean chck);
}
