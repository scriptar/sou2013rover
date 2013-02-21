package sou.rover.sou2013rover;

import java.util.Set;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


//Initial Activity -- Will be main screen, with menu options and instructions on connecting rover.
public class Main extends Activity {
	
    private static final int REQUEST_ENABLE_BT = 5;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	//called when activity is first created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final Button button = (Button) findViewById(R.id.Button01);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Log.v("test", "Button Pressed");
            	testBluetooth();
            }
        });
        
    }

    protected void testBluetooth(){
    	Log.v("test", "Bluetooth test");
    	
    	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	if (mBluetoothAdapter == null) {
    	    // Device does not support Bluetooth
        	Log.v("test", "Device does not support Bluetooth");
        	return;
    	}
    	
    	if (!mBluetoothAdapter.isEnabled()) {
        	Log.v("test", "Bluetooth Not Enabled, Enabling");
    	    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    	    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    	} else {
        	Log.v("test", "Bluetooth already Enabled");
    	}
    	
    	Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
    	// If there are paired devices
    	if (pairedDevices.size() > 0) {
    	    // Loop through paired devices
    	    for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a ListView
    	        // mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
    	    }
    	}
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handles menu item selection
        switch (item.getItemId()) {
            case R.id.bluetooth_settings:
                //TODO Open Bluetooth Activity
            	Log.d("test", "Open Bluetooth Activity");
            	//Return true if successfully handled
                return true;
            case R.id.telemetry_settings:
                //TODO Open Console & Sensor Activity
            	Log.d("test", "Open Console & Sensor Activity");
            	return true;
            case R.id.control_mode_settings:
            	//TODO Open Mode Selection Menu
            	Log.d("test", "Open Mode Selection Menu");
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
}
