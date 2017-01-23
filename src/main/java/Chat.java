import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static spark.Spark.*;
/**
 * Created by przemek on 16.01.17.
 */
public class Chat {
    static Map<String, Channel> channels=new ConcurrentHashMap<>();
    static Map<Session, Channel> channelsOf_=new ConcurrentHashMap<>();

    static void addChannel(String channelName){
        channels.put(channelName,new Channel(channelName));
    }
    static Channel channelOf_(Session user){
        return channelsOf_.get(user);
    }
    public static void main(String[] args){
        staticFileLocation("/public");
        webSocket("/chat", ChatWebSocketHandler.class);
        Chat.channels.put("chatBot", new Bot("chatBot"));
        init();
    }


    public static void addUser(String username, Session user, String channelName) {

        channels.get(channelName).addUser(username, user);
        channelsOf_.put(user,channels.get(channelName));

    }
}
