package com.alieeen.bulbdialclock;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alieeen.bulbdialclock.adapter.SlidingAdapter;
import com.alieeen.bulbdialclock.bluetooth.BluetoothSPP;
import com.alieeen.bulbdialclock.bluetooth.BluetoothState;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private SlidingAdapter mAdapter;
    private ViewPager mPager;

    private final String DEVICE_NAME = "HC-05";

    //region bluetooth variables
    private BluetoothSPP bluetooth;

    private static final String TAG = "Bulbdial";

    private Timer timer;
    private boolean timeChanged;
    private int minute;
    private int hour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupBluetooth();

        mAdapter = new SlidingAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timeChanged) {
                    bluetoothSendTime();
                    timeChanged = false;
                }
            }
        }, 0, 200);//put here time 1000 milliseconds=1 second

        Timer timer2 = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                bluetoothAutoConnect();

            }
        }, 0, 5000);//put here time 1000 milliseconds=1 second
    }

    public void bluetoothSend(String message) {

        if (bluetooth != null) {
            bluetooth.send(message, true);
            Log.i("BLUETOOTH SEND", "sent: " + message);

        } else {
            Log.i("BLUETOOTH", "Bluetooth not really connected");
        }

    }

    public void bluetoothTestSend(String message) {
        bluetooth.send(message, true);
        Log.i("BLUETOOTH SEND", "sent: " + message);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (bluetooth == null) {
            return;
        }
        if(!bluetooth.isBluetoothEnabled()) {
            bluetooth.enable();
        } else {
            if(!bluetooth.isServiceAvailable()) {
                bluetooth.setupService();
                bluetooth.startService(BluetoothState.DEVICE_OTHER);
                bluetoothAutoConnect();

            }
        }
    }

    private void bluetoothAutoConnect() {
        Log.i("Check", "start auto connection");
        bluetooth.autoConnect(DEVICE_NAME);
    }

    private void sendHandShake() {
        try {
            //bluetoothSend("0");
        } catch (Exception ex) {

        }
    }

    public void bluetoothSendTime() {
        String min = formatMinute(minute);
        String hou = formatHour(hour);

        bluetoothSend("A "+ min + " " + hou);
    }
    public void oldSend(int hour, int minute) {

        String min = formatMinute(minute);
        String hou = formatHour(hour);
        bluetoothSend("A "+ min + " " + hou);
    }

    private String formatMinute(int minute) {
        if (minute < 10) {
            return "0"+minute;
        } else {
            return String.valueOf(minute);
        }
    }

    private String formatHour(int hour) {
        if (hour > 11) {
            hour -= 12;
        }

        if (hour < 10) {
            return "0"+hour;
        } else {
            return String.valueOf(hour);
        }
    }


    private void setupBluetooth() {
        bluetooth = new BluetoothSPP(this);

        if (bluetooth == null) {
            return;
        }

        if(!bluetooth.isBluetoothAvailable()) {
            Toast.makeText(this
                    , "Buttonetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            //finish();
        }

        bluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(MainActivity.this
                        , "Connected to " + name
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() {
                Toast.makeText(MainActivity.this
                        , "Connection lost"
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() {
                Log.i("Check", "Unable to connect");

            }
        });
        bluetooth.setAutoConnectionListener(new BluetoothSPP.AutoConnectionListener() {
            public void onNewConnection(String name, String address) {
                Log.i("Check", "New Connection - " + name + " - " + address);

            }

            public void onAutoConnectionStarted() {
                Log.i("Check", "Auto menu_connection started");
            }
        });

        bluetooth.setBluetoothStateListener(new BluetoothSPP.BluetoothStateListener() {
            public void onServiceStateChanged(int state) {
                if (state == BluetoothState.STATE_CONNECTED) {
                    //printBluetoothInfo();
                }
                // Do something when successfully connected
                else if (state == BluetoothState.STATE_CONNECTING) {

                }
                // Do something while connecting
                else if (state == BluetoothState.STATE_LISTEN) {

                }
                // Do something when device is waiting for connection
                else if (state == BluetoothState.STATE_NONE) {
                    //printBluetoothError("");
                }
                // Do something when device don't have any connection
            }
        });
        //

        bluetooth.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                receiveData(message);
            }
        });


    }

    private void receiveData(String message) {

        Log.i(TAG, "received: " + message);

        if (message.contains("OK")) {
            Log.i(TAG, "handshake sucessfull");
        }


    }

    public void setMinute(int minute) {
        this.minute = minute;
        timeChanged = true;
    }

    public void setHour(int hour) {
        this.hour = hour;
        timeChanged = true;
    }
}
