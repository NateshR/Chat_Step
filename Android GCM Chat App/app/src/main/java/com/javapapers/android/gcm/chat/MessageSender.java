package com.javapapers.android.gcm.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Joe on 6/1/2014.
 */
public class MessageSender {
    private static final String TAG = "MessageSender";
    AsyncTask<Void, Void, String> sendTask;
    AtomicInteger ccsMsgId = new AtomicInteger();
    ProgressDialog progressDialog;
    Context context;


    public void sendMessage(final Bundle data, final GoogleCloudMessaging gcm) {

        sendTask = new AsyncTask<Void, Void, String>() {

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected String doInBackground(Void... params) {

                String id = Integer.toString(ccsMsgId.incrementAndGet());

                try {
                    Log.d(TAG, "messageid: " + id);
                    gcm.send(Config.GOOGLE_PROJECT_ID + "@gcm.googleapis.com", id,
                            data);
                    Log.d(TAG, "After gcm.send successful.");
                } catch (IOException e) {
                    Log.d(TAG, "Exception: " + e);
                    e.printStackTrace();
                }
                return "Message ID: " + id + " Sent.";
            }

            @Override
            protected void onPostExecute(String result) {
                sendTask = null;
                Log.d(TAG, "onPostExecute: result: " + result);
            }

        };
        sendTask.execute(null, null, null);
    }

}
