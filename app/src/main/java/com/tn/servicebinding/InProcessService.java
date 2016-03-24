package com.tn.servicebinding;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class InProcessService extends Service {

    private final IBinder serviceBinder = new InProcessBinder();

    public class InProcessBinder extends Binder {
        InProcessService getService() {
            return InProcessService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

}
