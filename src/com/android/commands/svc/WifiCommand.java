/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.commands.svc;

import android.os.ServiceManager;
import android.os.RemoteException;
import android.net.wifi.IWifiManager;
import android.content.Context;
import android.net.wifi.WifiInfo;

public class WifiCommand extends Svc.Command {
    public WifiCommand() {
        super("wifi");
    }

    public String shortHelp() {
        return "Control the Wi-Fi manager";
    }

    public String longHelp() {
        return shortHelp() + "\n"
                + "\n"
                + "usage: svc wifi [enable|disable]\n"
                + "         Turn Wi-Fi on or off.\n\n";
    }

    public void run(String[] args) {
        boolean validCommand = false;
        if (args.length >= 2) {
            boolean flag = false;

// start of get wifi mac address {
                       if ("getWifiInfo".equals(args[1])) {
                               IWifiManager wifiMgr = IWifiManager.Stub.asInterface(ServiceManager.getService(Context.WIFI_SERVICE));
                               try {
                                       WifiInfo wifiInfo = wifiMgr.getWifiInfo();
                                       System.out.println(wifiInfo);
                               }
                               catch (RemoteException e) {
                                       System.err.println("Wi-Fi operation failed: " + e);
                               }
                               return;
                       }
                       else if ("getAddress".equals(args[1])) {
                               IWifiManager wifiMgr = IWifiManager.Stub.asInterface(ServiceManager.getService(Context.WIFI_SERVICE));
                               try {
                                       WifiInfo wifiInfo = wifiMgr.getWifiInfo();
                                       System.out.println(wifiInfo.getMacAddress());
                               }
                               catch (RemoteException e) {
                                       System.err.println("Wi-Fi operation failed: " + e);
                               }
                               return;
                       }
// } end of get wifi mac address

            if ("enable".equals(args[1])) {
                flag = true;
                validCommand = true;
            } else if ("disable".equals(args[1])) {
                flag = false;
                validCommand = true;
            }
            if (validCommand) {
                IWifiManager wifiMgr
                        = IWifiManager.Stub.asInterface(ServiceManager.getService(Context.WIFI_SERVICE));
                if (wifiMgr == null) {
                    System.err.println("Wi-Fi service is not ready");
                    return;
                }
                try {
                    wifiMgr.setWifiEnabled("com.android.shell", flag);
                }
                catch (RemoteException e) {
                    System.err.println("Wi-Fi operation failed: " + e);
                }
                return;
            }
        }
        System.err.println(longHelp());
    }
}
