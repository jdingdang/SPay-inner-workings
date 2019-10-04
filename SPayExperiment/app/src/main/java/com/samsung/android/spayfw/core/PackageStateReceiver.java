/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.net.Uri
 *  android.os.IBinder
 *  android.os.Message
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Message;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.hce.SPayHCEReceiver;
import com.samsung.android.spayfw.core.hce.SPayHCEService;
import com.samsung.android.spayfw.core.j;

public class PackageStateReceiver
extends BroadcastReceiver {
    public static final void enable() {
        PaymentFrameworkApp.b(PackageStateReceiver.class);
    }

    String getPackageName(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            return uri.getSchemeSpecificPart();
        }
        return null;
    }

    public void onReceive(Context context, Intent intent) {
        String string = this.getPackageName(intent);
        c.i("PackageStateReceiver", "PackageName : " + string);
        c.i("PackageStateReceiver", "Data Removed : " + intent.getBooleanExtra("android.intent.extra.DATA_REMOVED", false));
        if (string != null && "com.samsung.android.spay".equals((Object)string) && intent.getBooleanExtra("android.intent.extra.DATA_REMOVED", false)) {
            c.i("PackageStateReceiver", "Initiate Reset");
            c.i("PackageStateReceiver", "PF detects wallet app uninstall and triggers reset notification");
            Message message = j.a(13, "FACTORY_RESET:PFE0BR01", new ICommonCallback(){

                public IBinder asBinder() {
                    return null;
                }

                @Override
                public void onFail(String string, int n2) {
                    c.i("PackageStateReceiver", "onFail : " + n2);
                }

                @Override
                public void onSuccess(String string) {
                    c.i("PackageStateReceiver", "onSuccess");
                }
            });
            PaymentFrameworkApp.az().sendMessage(message);
            PaymentFrameworkApp.d(PackageStateReceiver.class);
            SPayHCEService.disable();
            SPayHCEReceiver.disable();
        }
    }

}
