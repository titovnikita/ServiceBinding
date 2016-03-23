package com.tn.servicebinding;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyBindService extends Service {
    public static final int OPERATION_FINISHED = 1;

    private final IBinder binder = new ServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class ServiceBinder extends Binder {
        MyBindService getService() {
            return MyBindService.this;
        }
    }

    public int getNumber() {
        return 10;
    }
}
