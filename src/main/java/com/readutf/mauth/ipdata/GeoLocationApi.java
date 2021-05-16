package com.readutf.mauth.ipdata;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GeoLocationApi {

    public static String getCountry(String address)  {



        try {
            if(address.equalsIgnoreCase("127.0.0.1")) {
                return "Local Connection";
            }


            String ipDataString = "http://ip-api.com/json/" + address;

            URLConnection connection = new URL(ipDataString).openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = in.readLine();
            JsonObject jsonObject = new JsonParser().parse(line).getAsJsonObject();


            return jsonObject.get("country").getAsString();
        } catch (Exception e) {
            return "Unknown Location";
        }

    }

}
