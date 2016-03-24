package com.tn.servicebinding;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
    private final String TAG = getClass().getSimpleName();

    private IInnerProcessAidl aidlInterface;
    private boolean isServiceConnected = false;


    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, AidlService.class), serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isServiceConnected) {
            unbindService(serviceConnection);
            aidlInterface = null;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e(TAG, "Service connected");
            isServiceConnected = true;
            aidlInterface = IInnerProcessAidl.Stub.asInterface(iBinder);

            try {
                Toast.makeText(MainActivity.this, "Random number =  " + aidlInterface.getRandomNumber(15), Toast.LENGTH_SHORT).show();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e(TAG, "Service has unexpectedly disconnected");
            isServiceConnected = false;
        }
    };

}
