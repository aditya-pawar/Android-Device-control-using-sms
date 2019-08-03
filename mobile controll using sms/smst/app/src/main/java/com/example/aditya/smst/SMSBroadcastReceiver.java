package com.example.aditya.smst;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.icu.text.DateFormat;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class SMSBroadcastReceiver extends BroadcastReceiver {

        private SharedPreferences preferences;

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub






            if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
                Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from;
                if (bundle != null){
                    //---retrieve the SMS message received---
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            msg_from = msgs[i].getOriginatingAddress();
                            String Body = msgs[i].getMessageBody();


                            Toast.makeText(context, "SMS Received" + "\nfrom:" + msg_from + "\n body:" + Body, Toast.LENGTH_SHORT).show();

                            if (Body.compareTo("turn on flashlight") == 0) {
                                Camera camera = null;
                                camera = Camera.open();
                                 Camera.Parameters parameter = null;
                                parameter = camera.getParameters();
                                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                                camera.setParameters(parameter);
                                camera.startPreview();


                            }
                            else if(Body.compareTo("turn off flashlight")==0){
                                Camera camera = null;
                                camera = Camera.open();
                                Camera.Parameters parameter = null;
                                parameter = camera.getParameters();
                                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                                camera.setParameters(parameter);
                                camera.stopPreview();


                            }
                            else if (Body.compareTo("turn on wifi") == 0) {
                                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                                wifi.setWifiEnabled(true);


                            } else if (Body.compareTo("turn off wifi") == 0) {
                                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                                wifi.setWifiEnabled(false);
                            } else if (Body.compareTo("turn on data") == 0) {

                                final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                                final Class conmanClass = Class.forName(conman.getClass().getName());
                                final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                                iConnectivityManagerField.setAccessible(true);
                                final Object iConnectivityManager = iConnectivityManagerField.get(conman);
                                final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
                                final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
                                setMobileDataEnabledMethod.setAccessible(true);

                                setMobileDataEnabledMethod.invoke(iConnectivityManager, true);
                            }
                            else if (Body.compareTo("turn off data") == 0) {

                                final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                                final Class conmanClass = Class.forName(conman.getClass().getName());
                                final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                                iConnectivityManagerField.setAccessible(true);
                                final Object iConnectivityManager = iConnectivityManagerField.get(conman);
                                final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
                                final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("disableDataConnectivity", Boolean.TYPE);
                                setMobileDataEnabledMethod.setAccessible(true);

                                setMobileDataEnabledMethod.invoke(iConnectivityManager, true);
                            }
                            else if (Body.compareTo("silent mobile") == 0) {

                                AudioManager audiomanage = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                                audiomanage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                            } else if (Body.compareTo("general mobile") == 0) {
                                AudioManager audiomanage = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                                audiomanage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            } else if (Body.compareTo("vibrate mobile") == 0) {
                                AudioManager audiomanage = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                                audiomanage.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                            }


                            else if (Body.compareTo("ring mobile") == 0) {


                                Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                                Ringtone ringtoneSound = RingtoneManager.getRingtone(context.getApplicationContext(), ringtoneUri);

                                if (ringtoneSound != null) {
                                    ringtoneSound.play();
                                }
                            }
                            else if(Body.compareTo("stop ring")==0){
                                Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                                Ringtone ringtoneSound = RingtoneManager.getRingtone(context.getApplicationContext(), ringtoneUri);

                                if (ringtoneSound != null) {
                                    ringtoneSound.stop();
                                }
                            }
                            else if (Body.compareTo("turn on bluetooth") == 0) {


                                BluetoothAdapter bt=null;
                                bt=BluetoothAdapter.getDefaultAdapter();
                                bt.enable();
                            }
                            else if(Body.compareTo("turn off bluetooth")==0){

                                BluetoothAdapter bt=null;
                                bt=BluetoothAdapter.getDefaultAdapter();
                                bt.disable();
                            }
                            else if (Body.compareTo("location")==0){



                            }
                        }
                    }catch(Exception e){
                        // Log.d("Exception caught",e.getMessage());
                    }
                }
            }
        }
    }
