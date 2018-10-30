package rozklad.akai.org.pl.rozkadakai;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

import static rozklad.akai.org.pl.rozkadakai.Constants.KOSSA_LOG;

public class DataGetter {

    public static final int MAX_NAME_SIZE = 5;

    // TODO nie działą z polskimi znakami
    public static ArrayList<Stop> getStopsByName(String name, Context context) {
        ArrayList<Stop> stops = new ArrayList<>();
        String patternStart = "{\"pattern\":\"";
        String patternEnd = "\"}";
        patternStart = URLEncoder.encode(patternStart);
        patternEnd = URLEncoder.encode(patternEnd);

        Log.d(KOSSA_LOG, patternStart + name + patternEnd);
        String url = "https://www.peka.poznan.pl/vm/method.vm?method=getStopPoints&p0=%7B%22pattern%22%3A%22%C5%BCe%22%7D";
        //method=getStopPoints&p0=" + patternStart + name + patternEnd;

        HttpPostRequest postRequest = new HttpPostRequest();
        try {
            String responce = postRequest.execute(url).get();
            Toast.makeText(context, "Responce:  " + responce, Toast.LENGTH_LONG).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return stops;
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
                if (name.contains(pattern)) {
                    names.add(name);
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
