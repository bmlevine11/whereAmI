package com.Levine.whereami;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

//public class MainActivity extends android.support.v4.app.FragmentActivity {
public class MainActivity extends android.support.v4.app.FragmentActivity {
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
    	int check = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
    	if (check != ConnectionResult.SUCCESS){
    	String errorCode = GooglePlayServicesUtil.getErrorString(check);
    	AlertDialog warning = new AlertDialog.Builder(this).create();
    	warning.setTitle("Fuck");
    	warning.setMessage("You don't have Google Play Services guy Error: " + errorCode);
    	warning.setButton("Damn", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
    	warning.show();
    }
    }
    
}
