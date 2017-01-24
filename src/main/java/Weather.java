import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by przemek on 24.01.17.
 */
public class Weather {
    double temp;
    int pressure;
    int humidity;

    public Weather() throws JSONException, IOException {

        JSONObject json=JSONReader.readJsonFrom("http://api.openweathermap.org/data/2.5/weather?q=Cracow&appid=eec81fddf4db9350a73c513dc750104b");


        temp=json.getJSONObject("main").getDouble("temp")-273.15;
        pressure=json.getJSONObject("main").getInt("pressure");
        humidity=json.getJSONObject("main").getInt("humidity");
    }


    public String getWeather(){
        return "Dzisiaj w Krakowie temperatura wynosi "+temp+"\n°C. Ciśnienie wynosi "+pressure+"hpa,\n a wilgotność: "+humidity+"%.";
    }
}
