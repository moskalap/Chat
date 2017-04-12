import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by przemek on 19.01.17.
 */
public class JSONReader {


    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }

        return sb.toString();

    }


    public static JSONObject readJsonFrom(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();

        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String jsonText = readAll(rd);

        JSONObject json = new JSONObject(jsonText);
        is.close();
        return json;


    }


}
