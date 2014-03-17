package com.mobilise.it;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

public class WaitingDialog extends CordovaPlugin {
	
	private ProgressDialog waitingDialog = null;
	
	GPSTracker gps;
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if ("show".equals(action)) {
			String text = "Please wait";
			try {
				text = args.getString(0);
			} catch (Exception e) {
				LOG.d("WaitingDialog", "Text parameter not valid, using default");
			}
			showWaitingDialog(text);
			callbackContext.success();
			return true;
		} else if ("hide".equals(action)) {
			hideWaitingDialog();
			callbackContext.success();
			return true;
		} else if ("gps".equals(action)) {
			getLocation(callbackContext);
			//callbackContext.success();
			return true;			
		}else if ("ringtone".equals(action)) {			
			playRingtone();
			callbackContext.success();
			return true;
		}
		return false;
	}
	
	public void showWaitingDialog(String text) {
		if (waitingDialog == null) {
		waitingDialog = ProgressDialog.show(this.cordova.getActivity(), "", text);
		LOG.d("WaitingDialog", "Dialog shown, waiting hide command");
		}
	}
	
	public void hideWaitingDialog() {
		if (waitingDialog != null) {
			waitingDialog.dismiss();
			LOG.d("WaitingDialog", "Dialog dismissed");
			waitingDialog = null;
		} else {
			LOG.d("WaitingDialog", "Nothing to dismiss");
		}
	}
	
	public void playRingtone() {
		//Toast.makeText(this.cordova.getActivity().getApplicationContext(), "playRingtone method calling", Toast.LENGTH_LONG).show();
		try {
	        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	        Ringtone r = RingtoneManager.getRingtone(this.cordova.getActivity().getApplicationContext(), notification);
	        if(r == null) {
	        	notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
	        	r = RingtoneManager.getRingtone(this.cordova.getActivity().getApplicationContext(), notification);
	        }
	        r.play();
	    } catch (Exception e) {Toast.makeText(this.cordova.getActivity().getApplicationContext(), "playRingtone method exception", Toast.LENGTH_LONG).show();}
	}
	
	public void getLocation(CallbackContext callbackContext) {
		gps = new GPSTracker(this.cordova.getActivity());

		// check if GPS enabled		
        if(gps.canGetLocation()){
        	
        	double latitude = gps.getLatitude();
        	double longitude = gps.getLongitude();
        	
        	// \n is for new line
        	//Toast.makeText(this.cordova.getActivity().getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        	
        	JSONObject contactObject = new JSONObject();
            try {
                    contactObject.put("latitude", latitude);
                    contactObject.put("longitude", longitude);                    
            } catch (JSONException e) {
                    e.printStackTrace();
                    callbackContext.error("JSON error");
            }
        	callbackContext.success(contactObject);
        }else{
        	// can't get location
        	// GPS or Network is not enabled
        	// Ask user to enable GPS/network in settings
        	callbackContext.error("GPS is not enabled");
        	gps.showSettingsAlert();
        }	
	}
	
}
