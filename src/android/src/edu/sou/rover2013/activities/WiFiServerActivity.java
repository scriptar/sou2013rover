package edu.sou.rover2013.activities;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.sou.rover2013.BaseActivity;
import edu.sou.rover2013.R;
import edu.sou.rover2013.utility.BluetoothService;
import edu.sou.rover2013.utility.TCPService;

/**
 * Simple Test Screen that triggers wifi server mode. Will remove/hide/etc later
 * on.
 * 
 * @author Ryan Dempsey
 * 
 */
public class WiFiServerActivity extends BaseActivity {

	private TextView console;
	private EditText textPort;
	private BluetoothService connection;
	private Thread sThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wi_fi_server);

		// Get wireless connection
		connection = BluetoothService.getConnection();

		// Inline Button Listener
		final Button startButton = (Button) findViewById(R.id.startWifi);
		startButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				createTCPThread();
			}
		});

		// Setup Port Text Box
		textPort = (EditText) findViewById(R.id.wifiPortNumber);

		// Setup Console Out
		console = (TextView) findViewById(R.id.wifiConsole);
		console.setText("Wifi Page Opened\n");
		console.append("Current IP: " + getIPAddress(true) + "\n");

	}

	private void createTCPThread() {
		if (!validatePort(textPort.getText().toString())) {
			console.append("Invalid Port Number\n");
			return;
		}
		sThread = new Thread(new TCPService(connection,
				Integer.parseInt(textPort.getText().toString())));
		sThread.start();
		console.append("Thread instructed to start.\n");
	}

	private Boolean validatePort(String portArg) {
		try {
			int port = Integer.parseInt(portArg);
			if (port < 1 || port > 65535) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String getIPAddress(boolean useIPv4) {
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf
						.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress();
						boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
						if (useIPv4) {
							if (isIPv4)
								return sAddr;
						} else {
							if (!isIPv4) {
								int delim = sAddr.indexOf('%'); // drop ip6 port
																// suffix
								return delim < 0 ? sAddr : sAddr.substring(0,
										delim);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
		} // for now eat exceptions
		return "";
	}

}
