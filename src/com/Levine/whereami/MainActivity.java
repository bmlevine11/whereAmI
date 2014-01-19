package com.Levine.whereami;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.Menu;
import android.app.AlertDialog;
import android.content.IntentSender.SendIntentException;
import android.support.v4.app.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.*;
import android.widget.Toast;

public class MainActivity extends FragmentActivity
		implements
		com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks,
		com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener {

	private GoogleMap theMap;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	Location iAmHere;
	LocationClient locClient;
	LatLng currentLocation;

	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	// Checks to see if GooglePlayServices are connected.
	// I like my 'streamlined' way better.
	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Get the error code
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				// Set the dialog in the DialogFragment
				errorFragment.setDialog(errorDialog);
				// Show the error dialog in the DialogFragment
				errorFragment.show(getSupportFragmentManager(),
						"Location Updates");
			}
			return false;
		}
	}

	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		iAmHere = locClient.getLastLocation();
		
		currentLocation = new LatLng(iAmHere.getLatitude(),
				iAmHere.getLongitude());
	}

	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();

	}

	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					connectionResult.getErrorCode(), this,
					CONNECTION_FAILURE_RESOLUTION_REQUEST);
			ErrorDialogFragment bigProblem = new ErrorDialogFragment();
			bigProblem.setDialog(errorDialog);
			bigProblem.show(getSupportFragmentManager(),
					"Hopefully this never happens");

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Check for GooglePlayServices right away.
		int check = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());
		if (check != ConnectionResult.SUCCESS) {
			String errorCode = GooglePlayServicesUtil.getErrorString(check);
			AlertDialog warning = new AlertDialog.Builder(this).create();
			warning.setTitle("Fuck");
			warning.setMessage("You don't have Google Play Services guy Error: "
					+ errorCode);
			warning.show();
		}

		setContentView(R.layout.activity_main);
		theMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		theMap.setMyLocationEnabled(true);
		
		locClient = new LocationClient(this, this, this);
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void onStart() {
		super.onStart();
		// Connect the client.
		locClient.connect();
	}

	@Override
	protected void onResume() {
		Log.e("Status", "In onResume");
		super.onResume();
		iAmHere=locClient.getLastLocation();
		LatLng currentLocation = new LatLng(iAmHere.getLatitude(),
				iAmHere.getLongitude());
		theMap.addMarker(new MarkerOptions().position(currentLocation).title(
				"TJ Laser"));
		
	}

	protected void onStop() {
		// Disconnecting the client invalidates it.
		locClient.disconnect();
		super.onStop();
	}
}
