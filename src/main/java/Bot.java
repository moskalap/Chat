import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by przemek on 17.01.17.
 */
public class Bot extends Channel{


    public Bot(String channelName) {
        super(channelName);
    }

    static String getWeather(String city){
        double temp=0;
        int pressure=0;
        int humidity=0;
        try {
            JSONObject json=JSONReader.readJsonFrom("http://api.openweathermap.org/data/2.5/weather?q=Cracow&appid=eec81fddf4db9350a73c513dc750104b");


            temp=json.getJSONObject("main").getDouble("temp")-273.15;
            pressure=json.getJSONObject("main").getInt("pressure");
            humidity=json.getJSONObject("main").getInt("humidity");



        } catch (IOException e) {
            e.printStackTrace();
            return "Nie mogę zaaktualizować pogody.";
        } catch (JSONException e) {
            e.printStackTrace();
        }




        return "Dzisiaj w Krakowie temperatura wynosi "+temp+"\n°C. Ciśnienie wynosi "+pressure+"hpa,\n a wilgotność: "+humidity+"%.";
    }

    static String getTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date= new SimpleDateFormat("HH:mm:ss");
        return date.format(calendar.getTime());
    }
    static String getDate(){
        Calendar calendar = Calendar.getInstance();
        Date date= new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String[] days={"niedziela", "poniedziałek", "wtorek", "środa", "czwartek", "piątek", "sobota"};




        return "Dzisiaj jest "+days[dayOfWeek-1]+".";
    }
    public void broadcastMessage(String sender, String message) throws JSONException {

        System.out.println("tuok");
        System.out.println(message);
        if (!sender.equals("Serwer")) {

            try {

                Session user = this.getSessionOf_(sender);


                user.getRemote().sendString(String.valueOf(

                        new JSONObject().put("message", createHtmlMessageFromSender("Bot", this.executeTask(message)))

                ));
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }













    public static String executeTask(String message) {
        if(Pattern.matches(".*?godzina.*?", message)) return Bot.getTime();
        if(Pattern.matches(".*?pogoda.*?", message)) return Bot.getWeather("kraków");
        if(Pattern.matches(".*?tygodnia.*?", message)) return Bot.getDate();
        return "Nie mogę Cię zrozumieć.";
    }

    public static boolean findsTaskIn(String message) {
        if(Pattern.matches(".*?godzina.*?", message)) return true;
        if(Pattern.matches(".*?pogoda.*?", message)) return true;
        if(Pattern.matches(".*?tygodnia.*?", message)) return true;
        return false;
    }
}
