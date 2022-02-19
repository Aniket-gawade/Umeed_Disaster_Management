package com.aniket.healthcare;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aniket.healthcare.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private double lat;double lng;
    private int prox=10000;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private  String getUrl(double lat,double lng,String s){
StringBuilder googleurl=new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
googleurl.append("location="+ lat+","+lng);
googleurl.append("&radius="+prox);
googleurl.append("&type="+s);
        googleurl.append("&sensor=true");
        googleurl.append("&key="+"AIzaSyBqgjIp_9EZijKcZ_b1HToYwkxtushUDc4" );
        Log.d("GoogleMapActivity","url= "+googleurl.toString());
        return googleurl.toString();


    }

    public void onClick(View view)
    {
        String hospital="hospital";
        String firestation="Restaurants";
        String policestation="police";
        Object transferdata[]=new Object[2];
        getNearbyPlaces getNearbyPlaces=new getNearbyPlaces();


        switch (view.getId())
        {
            case R.id.searchbutton:
                EditText addressFeld=(EditText) findViewById(R.id.locationsearch);
                String address=addressFeld.getText().toString();
                List<Address> addressList=null;
                MarkerOptions usermarkerOptions=new MarkerOptions();
                if(!TextUtils.isEmpty(address))
                {
                    Geocoder geocoder=new Geocoder(this);
                    try{
                        addressList=geocoder.getFromLocationName(address,3);

                        if(addressList!=null)
                        {
                            for(int i=0;i<addressList.size();i++)

                            {
                                Address userAddress=addressList.get(i);
                                LatLng latLng=new LatLng(userAddress.getLatitude(),userAddress.getLongitude());

                                usermarkerOptions.position(latLng);
                                usermarkerOptions.title(address);
                                usermarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                                mMap.addMarker(usermarkerOptions);

                            }
                        }
                        else{
                            Toast.makeText(this,"Location not found",Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                }
                else{
                    Toast.makeText(this,"please write location",Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.hospitals_nearby:

                String url=getUrl(currentLocation.getLatitude(),currentLocation.getLongitude(),hospital);
                transferdata[0]=mMap;
                transferdata[1]=url;
                getNearbyPlaces.execute(transferdata);
                Toast.makeText(this,"seaching for nearby hospital",Toast.LENGTH_LONG).show();
                Toast.makeText(this,"showing nearby hospital",Toast.LENGTH_LONG).show();
                break;










            case R.id.firestations_nearby:
                mMap.clear();
                String url1=getUrl(currentLocation.getLatitude(),currentLocation.getLongitude(),"fire_station");
                transferdata[0]=mMap;
                transferdata[1]=url1;
                getNearbyPlaces.execute(transferdata);
                Toast.makeText(this,"seaching for nearby policeStation",Toast.LENGTH_LONG).show();
                Toast.makeText(this,"showing nearby police Station",Toast.LENGTH_LONG).show();
                break;











            case R.id.police_nearby:
            mMap.clear();
                String url2=getUrl(currentLocation.getLatitude(),currentLocation.getLongitude(),"police");
                transferdata[0]=mMap;
                transferdata[1]=url2;
                getNearbyPlaces.execute(transferdata);
                Toast.makeText(this,"seaching for nearby fireStation",Toast.LENGTH_LONG).show();
                Toast.makeText(this,"showing nearby fireStation",Toast.LENGTH_LONG).show();
                break;







        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        if (currentLocation != null) {
            LatLng sydney = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("Current Location is " +(float)currentLocation.getLatitude()+","+(float)currentLocation.getLongitude()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));

        }
    }
    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(@NonNull Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(),"This is my Toast message!", (int)Toast.LENGTH_LONG).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    //assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Toast.makeText(getApplicationContext()," location result is  " + locationResult, Toast.LENGTH_LONG).show();

                if (locationResult == null) {
                    Toast.makeText(getApplicationContext(),"current location is null ", Toast.LENGTH_LONG).show();

                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        lat=currentLocation.getLatitude();
                        lng=currentLocation.getLongitude();
                        Toast.makeText(getApplicationContext(),"current location is " + location.getLongitude(), Toast.LENGTH_LONG).show();

                        //TODO: UI updates.
                    }
                }
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (REQUEST_CODE) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
                break;
        }
    }


}