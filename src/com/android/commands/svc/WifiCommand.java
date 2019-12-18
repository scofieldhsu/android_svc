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
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.content.pm.ParceledListSlice;
import android.net.wifi.WifiConfiguration;

import java.util.List;


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
                                       if (wifiMgr.getWifiEnabledState() == WifiManager.WIFI_STATE_ENABLED) {
                                               WifiInfo wifiInfo = wifiMgr.getWifiInfo();
                                               System.out.println(wifiInfo.getMacAddress());
                                       }
                                       else
                                               System.err.println("wifi is not enabled!");
                               }
                               catch (RemoteException e) {
                                       System.err.println("Wi-Fi operation failed: " + e);
                               }
                               return;
                       }
// } end of get wifi mac address
			else if ("getConfig".equals(args[1])) {
				IWifiManager wifiMgr = IWifiManager.Stub.asInterface(ServiceManager.getService(Context.WIFI_SERVICE));
				try {
					ParceledListSlice<WifiConfiguration> parceledList = wifiMgr.getConfiguredNetworks();
					if (parceledList == null) {
						System.err.println("WifiConfiguration list is null! ");
						//return Collections.emptyList();
						return;
					}

					//List<WifiConfiguration> configList = wifiMgr.getConfiguredNetworks();
					List<WifiConfiguration> configList = parceledList.getList();
					System.out.println("number of WifiConfigurations: " + configList.size());
					for (WifiConfiguration c : configList) {
						System.out.println("\tWifiConfiguration: \n" + c);
					}
				}
				catch (RemoteException e) {
					System.err.println("Wi-Fi operation failed: " + e);
				}
				return;
			}
			else if ("connect".equals(args[1])) {
				/*
				connectToAccessPoint() of packages/apps/Car/Settings/src/com/android/car/settings/wifi/AddWifiFragment.java
				*/

				if (args.length<4) {
					System.err.println("command: svc wifi connect <none|wep|psk> <ssid> <password>, ex:\n\t"+
						"svc wifi connect psk aAP 12345678");
					return;
				}

				IWifiManager wifiMgr = IWifiManager.Stub.asInterface(ServiceManager.getService(Context.WIFI_SERVICE));

				WifiConfiguration wifiConfig = new WifiConfiguration();

				wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
				wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
				wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
				wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
				wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
				wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

				if ("none".equals(args[2])) {
					wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
					wifiConfig.allowedAuthAlgorithms.clear();
				}
				else if ("wep".equals(args[2])) {
					wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
					wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
					wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
					wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
					wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

					String password = args[4];
					//wifiConfig.wepKeys[0] = isHexString(password) ? password : "\"" + password + "\"";
					wifiConfig.wepTxKeyIndex = 0;
				}
				else if ("psk".equals(args[2])) {
					wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
					wifiConfig.preSharedKey = String.format("\"%s\"", args[4]);
				}
				else {
					System.err.println("command: svc wifi connect <none|wep|psk> <ssid> <password>, ex:\n\t"+
						"svc wifi connect psk aAP 12345678");
					return;
				}

				wifiConfig.SSID = String.format("\"%s\"", args[3]);

				try {
					//int netId = wifiMgr.addNetwork(wifiConfig);
					int netId = wifiMgr.addOrUpdateNetwork(wifiConfig);
					if (netId == -1) {
						System.err.println("addOrUpdateNetwork() failed!");
						return;
					}

					System.out.println("connect WifiConfiguration: \n" + wifiConfig);

					wifiMgr.enableNetwork(netId, true);
				}
				catch (RemoteException e) {
					System.err.println("Wi-Fi operation failed: " + e);
				}
				return;
			}

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
