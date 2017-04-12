import static spark.Spark.init;
import static spark.Spark.staticFileLocation;
import static spark.Spark.webSocket;

import org.json.JSONException;
import spark.*;

import java.io.IOException;


/**
 * Created by przemek on 24.01.17.
 */
public class Main {


    public static void main(String[] args){

        Chat chat= null;
        try {
            chat = new Chat ("Czat");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        staticFileLocation("/public");
        ChatWebSocketHandler handler=new ChatWebSocketHandler(chat);
        webSocket("/chat",  handler);
        init();
    }
}
