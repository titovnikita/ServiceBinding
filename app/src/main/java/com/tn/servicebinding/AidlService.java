package com.tn.servicebinding;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.Random;

public class AidlService extends Service {

    private final IInnerProcessAidl.Stub binder = new IInnerProcessAidl.Stub() {
        @Override
        public int getRandomNumber(int multiply) throws RemoteException {
            return new Random().nextInt() * multiply;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

}
