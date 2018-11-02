package rozklad.akai.org.pl.rozkadakai.Requests;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import rozklad.akai.org.pl.rozkadakai.StreamReader;

public class NamesPostRequest extends AsyncTask<String, Void, String> {

    public static final String REQUEST_METHOD = "POST";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        String pattern = strings[0];
        String result = "";
        try {
            url = new URL("https://www.peka.poznan.pl/vm/method.vm");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8");

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.connect();

            //OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            String payload = "method=getStopPoints&p0=" + pattern;
            byte[] postData = payload.getBytes(StandardCharsets.UTF_8);
            connection.getOutputStream().write(postData);
            result = StreamReader.readStream(connection.getInputStream());
            connection.disconnect();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
