package com.okandroid.block.core;

import android.os.IBinder;

interface ICoreServicesManager {

    IBinder getCoreService(String serviceName);

}
