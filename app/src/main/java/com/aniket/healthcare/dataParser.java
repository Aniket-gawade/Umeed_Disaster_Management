package com.aniket.healthcare;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class dataParser {

    //get place name latt ,long etc
    private HashMap<String,String> getsinglenearbyplace(JSONObject googlePlaceJSON)
    {
            HashMap<String,String>googleplacemap=new HashMap<>();
            String nameofplace="--NA--";
            String vicinity="--NA--";
            String lattitude="";
            String longitude="";
            String reference="";
            try{
                if(!googlePlaceJSON.isNull("name"))
                {
                    nameofplace=googlePlaceJSON.getString("name");
                }
                if(!googlePlaceJSON.isNull("vicinity")){
                    vicinity=googlePlaceJSON.getString("vicinity");

                }
                lattitude=googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude=googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");
                reference=googlePlaceJSON.getString("reference");
                googleplacemap.put("place_name",nameofplace);
                googleplacemap.put("vicinity",vicinity);
                googleplacemap.put("lat",lattitude);
                googleplacemap.put("lng",longitude);
                googleplacemap.put("reference",reference);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return googleplacemap;
    }

    private List<HashMap<String,String>> getallnearbyplaces(JSONArray jsonArray){
        int counter=jsonArray.length();
        List<HashMap<String,String>> nearbyplaceslist=new ArrayList<>();
        HashMap<String,String> nearbyplacemap=null;
        for(int i=0;i<counter;i++) {
            try {
                nearbyplacemap = getsinglenearbyplace((JSONObject) jsonArray.get(i));
                nearbyplaceslist.add(nearbyplacemap);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
return nearbyplaceslist;
    }

    public List<HashMap<String,String>> parseString(String jsondata)
    {

        JSONArray jsonArray=null;
        JSONObject jsonObject;
        try{
            jsonObject=new JSONObject(jsondata);
            jsonArray=jsonObject.getJSONArray("results");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getallnearbyplaces(jsonArray);
    }
    public String getStateFromJson(String jsondata) {

        String state = "";
        try
        {
            JSONObject jsonobject = new JSONObject((String) jsondata);
            JSONArray jsonarray = jsonobject.getJSONArray("results");
            jsonobject = jsonarray.getJSONObject(0);
            jsonarray = jsonobject.getJSONArray("address_components");
            int arrlen = jsonarray.length();
            jsonobject = jsonarray.getJSONObject(arrlen-3);

            state = jsonobject.getString("long_name");
        } catch (Exception e) {
            Log.d("State", "State Parsing error");
            e.printStackTrace();
        }

        return state;

    }


}
