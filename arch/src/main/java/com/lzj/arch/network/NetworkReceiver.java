/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

/**
 * 网络广播接收器。
 *
 * @author 吴吉林
 */
public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        switch (action) {
            case CONNECTIVITY_ACTION:
                NetworkManager.checkConnectivity(true);
                break;
        }
    }
}
