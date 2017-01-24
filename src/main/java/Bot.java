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
Weather weather;


    public Bot(String channelName) {
        super(channelName);
        try {
            this.weather= new Weather();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getWeather(String city){




        return  weather.getWeather();
    }

    public String getTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date= new SimpleDateFormat("HH:mm:ss");
        return date.format(calendar.getTime());
    }
    public String getDate(){
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


               if(findsTaskIn(message))

               {user.getRemote().sendString(String.valueOf(

                        new JSONObject().put("message", this.executeTask(message)).put("broadcaster", "bot")

                ));}
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }













    public  String executeTask(String message) {
        if(Pattern.matches(".*?godzina.*?", message)) return this.getTime();
        if(Pattern.matches(".*?pogoda.*?", message)) return this.getWeather("kraków");
        if(Pattern.matches(".*?tygodnia.*?", message)) return this.getDate();
        return "Nie mogę Cię zrozumieć.";
    }

    public  boolean findsTaskIn(String message) {
        if(Pattern.matches(".*?godzina.*?", message)) return true;
        if(Pattern.matches(".*?pogoda.*?", message)) return true;
        if(Pattern.matches(".*?tygodnia.*?", message)) return true;
        return false;
    }
}
