package com.tn.servicebinding;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends ActionBarActivity {

    private ServiceMessagesHandler serviceMessagesHandler;
    private Messenger messenger;
    private boolean isServiceConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceMessagesHandler = new ServiceMessagesHandler(this);
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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serviceMessagesHandler = null;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(getClass().getSimpleName(), "ServiceConnection connected");
            messenger = new Messenger(iBinder);
            isServiceConnected = true;

            Message msg = Message.obtain(null, MyBindService.OPERATION_START);
            msg.replyTo = new Messenger(serviceMessagesHandler);
            try {
                messenger.send(msg);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(getClass().getSimpleName(), "ServiceConnection disconnected");
            isServiceConnected = false;
            messenger = null;
        }
    };

    private static class ServiceMessagesHandler extends Handler {
        private final WeakReference<Context> contextWeakReference;

        public ServiceMessagesHandler(Context context) {
            contextWeakReference = new WeakReference<Context>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            Context reference = contextWeakReference.get();
            if (reference != null) {
                switch (msg.what) {
                    case MyBindService.OPERATION_FINISHED:
                        Toast.makeText(reference, "Operation finished.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        }
    }
}
