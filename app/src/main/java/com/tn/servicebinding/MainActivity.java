package com.tn.servicebinding;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends ActionBarActivity {

    private MyBindService service;
    private ServiceMessagesHandler serviceMessagesHandler;
    private Messenger messenger;
    private boolean isServiceConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceMessagesHandler = new ServiceMessagesHandler(this);
        messenger = new Messenger(serviceMessagesHandler);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MyBindService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isServiceConnected) {
            unbindService(serviceConnection);
            service = null;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(getClass().getSimpleName(), "ServiceConnection connected");
            MyBindService.ServiceBinder binder = (MyBindService.ServiceBinder) iBinder;
            service = binder.getService();
            isServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(getClass().getSimpleName(), "ServiceConnection disconnected");
            isServiceConnected = false;
        }
    };

    private static class ServiceMessagesHandler extends Handler {
        private final WeakReference<MainActivity> myClassWeakReference;

        public ServiceMessagesHandler(MainActivity myClassInstance) {
            myClassWeakReference = new WeakReference<MainActivity>(myClassInstance);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity reference = myClassWeakReference.get();
            if (reference != null) {
                switch (msg.what) {
                    case MyBindService.OPERATION_FINISHED:
                        Toast.makeText(reference, "Operation finished." + reference.service.getNumber(), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        }
    }
}
