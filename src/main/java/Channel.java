import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static j2html.TagCreator.*;
/**
 * Created by przemek on 16.01.17.
 */
public class Channel {

    private final String channelName;
    private Map<Session, String> users=new ConcurrentHashMap<>();

    public Channel(String channelName){
    this.channelName=channelName;

}
    public String getName(){
        return channelName;
    }

    public void addUser(String nick, Session session){
        users.put(session,nick);
    }

    public void sendUsersList() {
        users.keySet().stream().filter(Session::isOpen).forEach(session -> {
                    try {
                        String a=String.valueOf(new JSONObject().put("users",new HashSet<String>(this.getUsers().values())));
                        System.out.println(a);
                        session.getRemote().sendString( a);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

        );

    }
    public String get(Session session){
        return users.get(session);
    }

    public void broadcastMessage(String sender, String message) {

        System.out.println(sender+" mówi "+message);



        users.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("message", this.createHtmlMessageFromSender(sender, message))

                ));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });





    }
    private String createHtmlMessageFromSender(String sender, String message) {
        return article().with(
                b(sender + " napisał:"),
                p(message),
                span().withClass("timestamp").withText(new SimpleDateFormat("HH:mm:ss").format(new Date()))
        ).render();
    }

    public void remove(Session user) {

    }

    public Map<Session,String> getUsers() {

        return users;
    }
}

