package com.aniket.healthcare;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class getNearbyPlaces extends AsyncTask<Object, String,String> {

private  String googleplaceData,url;
private GoogleMap mMap;


    @Override
    protected String doInBackground(Object... objects)
    {
    mMap=(GoogleMap) objects[0];
    url=(String)objects[1];
    downloadUrl downloadUrl=new
            downloadUrl();
        try {
            googleplaceData=downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleplaceData;
    }
    protected void onPostExecute(String s)
    {
        List<HashMap<String,String>> nearbyplaceslist=null;
        dataParser dataParser=new dataParser();
        nearbyplaceslist=dataParser.parseString(s);
        displaynamebyplaces(nearbyplaceslist);
    }

    //show all places in the list
    private void displaynamebyplaces(List<HashMap<String,String>> nearbyplaceslist)
    {
        for(int i=0;i<nearbyplaceslist.size();i++)
        {
            MarkerOptions markerOptions=new MarkerOptions();
            HashMap<String,String> googlenearbyPlace=nearbyplaceslist.get(i);
            String nameofplace=googlenearbyPlace.get("place_name");
            String vicinity=googlenearbyPlace.get("vicinity");
            double lat=Double.parseDouble(googlenearbyPlace.get("lat"));
            double lng=Double.parseDouble(googlenearbyPlace.get("lng"));
            LatLng latLng=new LatLng(lat,lng);

           markerOptions.position(latLng);
            markerOptions.title(nameofplace+" :"+vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            mMap.addMarker(markerOptions);
        }




    }


}
