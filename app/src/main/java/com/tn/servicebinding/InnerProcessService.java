package com.tn.servicebinding;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class InnerProcessService extends Service {
    public static final int OPERATION_START = 1;
    public static final int OPERATION_FINISHED = 2;

    private Messenger messenger;
    private MessageHandler msgHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        msgHandler = new MessageHandler(this);
        messenger = new Messenger(msgHandler);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        msgHandler = null;
        messenger = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    private static class MessageHandler extends Handler {
        private final WeakReference<Context> contextWeakReference;

        public MessageHandler(Context context) {
            contextWeakReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            Context reference = contextWeakReference.get();
            if (reference != null) {
                final Messenger replyTo = msg.replyTo;
                switch (msg.what) {
                    case OPERATION_START:
                        Toast.makeText(reference, "Operation started.", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Message message = Message.obtain(null, OPERATION_FINISHED);
                                    replyTo.send(message);
                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }, 4000);
                        break;
                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        }
    }

}
