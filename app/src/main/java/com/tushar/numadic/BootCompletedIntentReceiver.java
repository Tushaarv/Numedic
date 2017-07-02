package com.tushar.numadic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Tushar Vengurlekar
 * Created on 02/07/17.
 */

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            serviceStart(context);
        }
    }

    private void serviceStart(Context context) {

        DataManager dataManager = new DataManager(context);
        if (dataManager.getServiceStatus()) {
            context.startService(new Intent(context, NumadicService.class));
            dataManager.setServiceStatus(true);
            dataManager.setServiceStartTime(TimeManager.getCurrentUTCTime());
        }
    }
}
