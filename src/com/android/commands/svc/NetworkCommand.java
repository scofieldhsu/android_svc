/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
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
import android.content.Context;
import com.android.internal.telephony.ITelephony;
import android.os.INetworkManagementService;
import android.os.SystemProperties;


public class NetworkCommand extends Svc.Command {
	INetworkManagementService netd;
	String sWifiIface;
	
	public NetworkCommand() {
		super("network");

		netd = INetworkManagementService.Stub.asInterface(
			ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE));

		sWifiIface = SystemProperties.get("wifi.interface", "ath0");
	}

	public String shortHelp() {
		return "Manage network";
	}

	public String longHelp() {
		return shortHelp() + "\n"
				+ "\n"
				+ "usage: svc network ipv6 " + sWifiIface + " [enable|disable]\n"
				+ " 		Turn " + sWifiIface + " ipv6 on or off.\n\n";
	}

	public void run(String[] args) {
		boolean validIpv6Cmd = false;
		if (args.length > 2) {
			boolean flag = false;

			if ("ipv6".equals(args[1]) && sWifiIface.equals(args[2])) {
				if (args.length > 3) {
					if ("enable".equals(args[3])) {
						flag = true;
						validIpv6Cmd = true;
					}
					else if ("disable".equals(args[3])) {
						flag = false;
						validIpv6Cmd = true;
					}
				}
			}

			if (validIpv6Cmd) {
				try {
					if (flag)
						netd.enableIpv6(sWifiIface);
					else
						netd.disableIpv6(sWifiIface);
				}
				catch (RemoteException e) {
					System.err.println("network operation failed: " + e);
				}
				return;
			}
		}

		System.err.println(longHelp());
	}
}

