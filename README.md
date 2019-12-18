# android_svc


* $ adb shell svc

Available commands:

help         Show information about the subcommands

power        Control the power manager

data         Control mobile data connectivity

wifi         Control the Wi-Fi manager

usb          Control Usb state

nfc          Control NFC functions

bluetooth    Control Bluetooth service


* frameworks/base/cmds/svc/ of original Android source code

https://android.googlesource.com/platform/frameworks/base/+/refs/heads/oreo-cts-release/cmds/svc/


* add svc command 'svc wifi connect <none|wep|psk> <ssid> <password>' to connect wifi


* add ipv6 network commands:
'adb shell svc network ipv6 wlan0 disable' to disable ipv6
'adb shell svc network ipv6 wlan0 enable' to enable ipv6

commands to check: (please check inet6 field)
$ adb shell svc network ipv6 wlan0 enable

$ adb shell ifconfig wlan0
wlan0 Link encap:Ethernet HWaddr 00:11:22:33:44:55 Driver cnss_ar6k_wlan
inet addr:192.168.50.182 Bcast:192.168.50.255 Mask:255.255.255.0
inet6 addr: fe80::211:22ff:fe33:4455/64 Scope: Link
UP BROADCAST RUNNING MULTICAST MTU:1500 Metric:1
RX packets:4090 errors:0 dropped:0 overruns:0 frame:0
TX packets:643 errors:0 dropped:0 overruns:0 carrier:0
collisions:0 txqueuelen:3000
RX bytes:825162 TX bytes:147004

$ adb shell svc network ipv6 wlan0 disable

$ adb shell ifconfig wlan0
wlan0 Link encap:Ethernet HWaddr 00:11:22:33:44:55 Driver cnss_ar6k_wlan
inet addr:192.168.50.182 Bcast:192.168.50.255 Mask:255.255.255.0
UP BROADCAST RUNNING MULTICAST MTU:1500 Metric:1
RX packets:4115 errors:0 dropped:0 overruns:0 frame:0
TX packets:643 errors:0 dropped:0 overruns:0 carrier:0
collisions:0 txqueuelen:3000
RX bytes:828916 TX bytes:147004

