/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GoEuroProjectPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Benoit
 */
public class GoEuroProjectClass {
    public static void getInfosFromUrl(String clientInput) {
        //Adding input from user to URL
        String url = "http://api.goeuro.com/api/v2/position/suggest/en/" + clientInput;
        StringBuilder builder = new StringBuilder();
        
        //The csv file will be located at the root's project.
        String fileAd = System.getProperty("user.dir") + "/file.csv";
        File file=new File(fileAd);
        BufferedWriter writer;
        try (InputStream is = new URL(url).openConnection().getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            //Retrieving the web page content.
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                builder.append(line + "\n");
            }
            try {
                //Using JSON API to retrieve the different informations.
                JSONParser parser = new JSONParser();
                Object resultObject = parser.parse(builder.toString());
                
                writer=new BufferedWriter(new FileWriter(file, false));
                String lineFeed=System.getProperty("line.separator");

                if (resultObject instanceof JSONArray) {
                    JSONArray array = (JSONArray) resultObject;
                    for (Object object : array) {
                        JSONObject obj = (JSONObject) object;
                        StringBuilder outStr = new StringBuilder();
                        outStr.append(obj.get("_id"));
                        outStr.append(", ");
                        outStr.append(obj.get("name"));
                        outStr.append(", ");
                        outStr.append(obj.get("type"));
                        outStr.append(", ");
                        JSONObject objGeoLoc=(JSONObject) obj.get("geo_position");                        
                        outStr.append(objGeoLoc.get("latitude"));
                        outStr.append(", ");
                        outStr.append(objGeoLoc.get("longitude"));
                        outStr.append(lineFeed);    
                        //Writing those informations into csv file.
                        writer.write(outStr.toString());                        
                    }
                }
                writer.flush();
                writer.close();
            } catch (ParseException pe) {
                System.out.println(pe.getMessage());
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public static void main(String[] args) {
        //Get the user input.
        if(args.length == 1){
            getInfosFromUrl(args[0]);
        }else{
        //If user input is blank, the system print an error.
            getInfosFromUrl("");            
        }
    }
}
