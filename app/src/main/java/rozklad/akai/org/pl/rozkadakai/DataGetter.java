package rozklad.akai.org.pl.rozkadakai;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import rozklad.akai.org.pl.rozkadakai.Data.Place;
import rozklad.akai.org.pl.rozkadakai.Data.Stop;
import rozklad.akai.org.pl.rozkadakai.Data.Tram;
import rozklad.akai.org.pl.rozkadakai.Requests.HttpGetRequest;
import rozklad.akai.org.pl.rozkadakai.Requests.HttpPostRequest;
import rozklad.akai.org.pl.rozkadakai.Requests.NamesPostRequest;
import rozklad.akai.org.pl.rozkadakai.Requests.TagPostRequest;

import static rozklad.akai.org.pl.rozkadakai.Constants.KOSSA_LOG;

public class DataGetter {

    public static final int MAX_NAME_SIZE = 5;


    public static ArrayList<String> getStopsByPattern(String name) {
        ArrayList<String> stops = new ArrayList<>();
        String pattern = "{\"pattern\":\"" + name + "\"}";

        try {
            NamesPostRequest postRequest = new NamesPostRequest();
            String responce = postRequest.execute(pattern).get();
            JSONObject jsonObject = new JSONObject(responce);
            JSONArray stopsArray = jsonObject.getJSONArray("success");
            for (int i = 0; i < stopsArray.length(); i++) {
                JSONObject jsonStop = stopsArray.getJSONObject(i);
                String stopName = jsonStop.getString("name");
                stops.add(stopName);
            }
        } catch (ExecutionException e) {

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return stops;
    }

    public static Stop getStopByName(String name) {
        ArrayList<String> symbols = new ArrayList<>();
        String pattern = "{\"name\":\"" + name + "\"}";

        try {
            TagPostRequest postRequest = new TagPostRequest();
            String responce = postRequest.execute(pattern).get();
            JSONObject jsonObject = new JSONObject(responce);
            JSONObject bollards = jsonObject.getJSONObject("success");
            JSONArray tags = bollards.getJSONArray("bollards");
            for (int i = 0; i < tags.length(); i++) {
                JSONObject stop = tags.getJSONObject(i);
                JSONObject bollard = stop.getJSONObject("bollard");
                String symbol = bollard.getString("tag");
                symbols.add(symbol);
            }
        } catch (ExecutionException e) {

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Stop(name, symbols);
    }

    public static ArrayList<Tram> getTramsDepartures(String stopSymbol) {
        ArrayList<Tram> trams = new ArrayList<>();
        String przystanek = "{\"symbol\":\"" + stopSymbol + "\"}";
        przystanek = URLEncoder.encode(przystanek);
        long date = new Date().getTime() + 3600000;
        String url = "https://www.peka.poznan.pl/vm/method.vm?ts=" + date + "&method=getTimes&p0=" + przystanek;

        HttpPostRequest postRequest = new HttpPostRequest();
        try {
            String responce = postRequest.execute(url).get();

            if (responce != null) {
                JSONObject object = new JSONObject(responce);
                JSONObject succes = object.getJSONObject("success");

                JSONObject bollard = succes.getJSONObject("bollard");
                String name = bollard.getString("name");

                JSONArray tramList = succes.getJSONArray("times");

                for (int i = 0; i < tramList.length(); i++) {
                    JSONObject tram = tramList.getJSONObject(i);
                    String line = tram.getString("line");
                    String departure = tram.getString("departure");
                    String direction = tram.getString("direction");
                    boolean realTime = tram.getBoolean("realTime");

                    trams.add(new Tram(departure, direction, line, realTime));
                }
            } else {
                return null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trams;
    }

    ;

    public static JSONArray getBikePlaces() {
        try {
            String myUrl = "https://api.nextbike.net/maps/nextbike-live.json?city=192";
            HttpGetRequest getRequest = new HttpGetRequest();
            String responce = getRequest.execute(myUrl).get();
            //Log.d(KOSSA_LOG, "Responce: " + responce);
            JSONObject object = new JSONObject(responce);
            JSONArray countriesArray = object.getJSONArray("countries");
            JSONObject poznanObject = (JSONObject) countriesArray.get(0);
            JSONArray citiesArray = (JSONArray) poznanObject.get("cities");
            JSONObject cityObject = (JSONObject) citiesArray.get(0);
            JSONArray places = (JSONArray) cityObject.get("places");
            return places;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Place getPlaceByName(JSONArray places, String name) {
        for (int i = 0; i < places.length(); i++) {
            try {
                JSONObject object = (JSONObject) places.get(i);
                if (object.getString("name").compareTo(name) == 0) {
                    JSONArray bikesList = object.getJSONArray("bike_list");
                    int count = bikesList.length();
                    Log.d(KOSSA_LOG, "i = " + i + " " + object.getString("name") + " count: " + count);
                    return new Place(name, count);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public static ArrayList<String> getPlacesNamesByPattern(String pattern, JSONArray places) {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < places.length(); i++) {
            try {
                JSONObject object = places.getJSONObject(i);
                String name = object.getString("name");

                if (name.toLowerCase().contains(pattern.toLowerCase())) {
                    names.add(name);
                    Log.d(KOSSA_LOG, "Name: " + name);
                }
                if (names.size() > MAX_NAME_SIZE) {
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return names;
    }
}
