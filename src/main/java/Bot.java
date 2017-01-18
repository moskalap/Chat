import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Created by przemek on 17.01.17.
 */
public class Bot {



    static String getWeather(String city){


    return "pogoda";
    }

    static String getTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date= new SimpleDateFormat("HH:mm:ss");
        return date.format(calendar.getTime());
    }
    static String getDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date= new SimpleDateFormat("HH:mm:ss");
        return
   "dzien tygodnia";
    }

    public static String executeTask(String message) {
            if(Pattern.matches(".*?godzina.*?", message)) return Bot.getTime();
        if(Pattern.matches(".*?pogoda.*?", message)) return Bot.getWeather("kraków");
        if(Pattern.matches(".*?tygodnia.*?", message)) return Bot.getDate();
        return "";
    }

    public static boolean findsTaskIn(String message) {
        if(Pattern.matches(".*?godzina.*?", message)) return true;
        if(Pattern.matches(".*?pogoda.*?", message)) return true;
        if(Pattern.matches(".*?tygodnia.*?", message)) return true;
        return false;
    }
}
