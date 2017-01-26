import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static spark.Spark.*;
/**
 * Created by przemek on 16.01.17.
 */
public class Chat {


    private final String chatname;

    public Chat(String chatname){
        this.chatname = chatname;
        channels.put("chatBot", new Bot("chatBot"));
    }


    private Map<String, Channel> channels=new ConcurrentHashMap<>();
    private Map<Session, Channel> channelsOf_=new ConcurrentHashMap<>();

    public void addChannel(String channelName){
        channels.put(channelName,new Channel(channelName));
    }
    public Channel channelOf_(Session user){
        return channelsOf_.get(user);
    }



    public void addUser(String username, Session user, String channelName) {
       try {
           if (channels.get(channelName).has(username)) throw new IllegalArgumentException();

           channels.get(channelName).addUser(username, user);
           channelsOf_.put(user, channels.get(channelName));
       }
       catch(IllegalArgumentException e){

           try {
               user.getRemote().sendString( String.valueOf(new JSONObject().put("error", "Użytkownik juz istnieje!")));
           } catch (IOException e1) {
               e1.printStackTrace();
           } catch (JSONException e1) {
               e1.printStackTrace();
           }
       }

    }
    public Map<String,Channel> getChannels(){
        return this.channels;
    }
    public void deleteUser(Session user){
        channelsOf_.remove(user);
    }
}
