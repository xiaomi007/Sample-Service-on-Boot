Sample Android apps to Set Alarm
===

Choose 2 dates, press valid will set the Alarm Manager to start an Wakeful Broadcast Receiver.
The WBR will start the service and keep the phone awake until it received WakeUpOnBoot.completeWakefulIntent(intent);.

If the phone reboot, the Alarm Manager will be reset. So on Boot complete, start the Wakeful Broadcast Receiver to set again the Alarm Manager (only if the end date is prior to the actual date)



