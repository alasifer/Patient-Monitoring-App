package BaseSystem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * this class responsible to get the data from the api
 *
 * @authors: AHMED ABDULWAHID OMAR ALASAIFER & HOW CARL KIT
 * Monash University Malaysia
 * @last_edited: 27/April/2019
 */
public class Detail {

    private String type;
    private Integer id;
    private String paramt;

    /**
     * @param type type needs to be fetched (e.g patient, practitioner, observation,,,etc)
     * @param id id of the type provided (if -1 is given it means that we need to fetch the whole list of the given type)
     * paramt -> is used to get data based on given condition (e.g: count, sort, summary..)
     */
    public Detail(String type, int id, String paramt) {
        this.type = type;
        this.id = id;
        this.paramt = paramt;
    }

    /**
     * @return json object based on the passed parameter to the constructor
     * @throws IOException
     * @throws JSONException
     */
    public JSONObject getDetail() throws IOException, JSONException {

        StringBuilder BASE_URL = new StringBuilder("http://hapi-fhir.erc.monash.edu:8080/baseDstu3/");
        BASE_URL.append(type);
        BASE_URL.append("/");

        if (id != null) {
            BASE_URL.append(id.toString());
        }

        if (paramt != null){
            BASE_URL.append(paramt);
        }

        URL url = new URL(BASE_URL.toString());

        return estConnect(url);
    }

    /**
     * get data based on string code
     *
     * @return data based on the given code
     */


    public JSONObject filter(String code) throws IOException, JSONException {
        StringBuilder BASE_URL = new StringBuilder("http://hapi-fhir.erc.monash.edu:8080/baseDstu3/");
        BASE_URL.append(type);
        BASE_URL.append("/");

        BASE_URL.append("?code=");
        BASE_URL.append(code);
        BASE_URL.append("&subject=Patient/");
        BASE_URL.append(this.id.toString());
        BASE_URL.append("&_sort=date");
        URL url = new URL(BASE_URL.toString());

        return estConnect(url);

    }

    /**
     * establish the connection to the api
     *
     * @param url: api url
     * @return json object of the given url
     * */

    private JSONObject estConnect(URL url) throws IOException, JSONException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/fhir+json");

        BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

        String output;

        StringBuilder content = new StringBuilder();
        while ((output = br.readLine()) != null) {
            content.append(output);
        }
        JSONObject obj = new JSONObject(content.toString());

        return obj;

    }

}
