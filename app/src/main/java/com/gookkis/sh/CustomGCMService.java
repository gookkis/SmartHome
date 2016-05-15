package com.gookkis.sh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import co.mobiwise.fastgcm.GCMListenerService;

public class CustomGCMService extends GCMListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);

        Log.d("Notif", "onMessageReceived: " + data.toString());

        Intent intent = new Intent(getApplicationContext(), WarningActivity.class);
        intent.putExtra("title", data.get("title").toString());
        intent.putExtra("message", data.get("message").toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);


    }
} 