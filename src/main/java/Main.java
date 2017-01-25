import static spark.Spark.init;
import static spark.Spark.staticFileLocation;
import static spark.Spark.webSocket;
import spark.*;


/**
 * Created by przemek on 24.01.17.
 */
public class Main {


    public static void main(String[] args){

        Chat chat= new Chat ("Czat");
        staticFileLocation("/public");
        ChatWebSocketHandler handler=new ChatWebSocketHandler(chat);
        webSocket("/chat",  handler);
        init();
    }
}
