import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static j2html.TagCreator.*;

/**
 * Created by przemek on 16.01.17.
 */
public class Channel {

    protected final String channelName;
    protected Map<Session, String> users = new ConcurrentHashMap<>();
    protected Map<String, Session> usersByName = new ConcurrentHashMap<>();




    public Channel(String channelName) {
        this.channelName = channelName;

    }

    public String getName() {
        return channelName;
    }

    public void addUser(String nick, Session session) {
        usersByName.put(nick, session);
        users.put(session, nick);
    }

    public void sendUsersList() {
        users.keySet().stream().filter(Session::isOpen).forEach(session -> {
                    try {
                        String a = String.valueOf(new JSONObject().put("users", new HashSet<String>(this.getUsers().values())));
                        System.out.println(a);
                        session.getRemote().sendString(a);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

        );

    }

    public void toDelete(String person){
        users.keySet().stream().filter(Session::isOpen).forEach(session -> {
                    try {
                        String a = String.valueOf(new JSONObject().put("remove", person));
                        System.out.println(a);
                        session.getRemote().sendString(a);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

        );

    } public void toAdd(String person){
        users.keySet().stream().filter(Session::isOpen).forEach(session -> {
                    try {
                        String a = String.valueOf(new JSONObject().put("add", person));
                        System.out.println(a);
                        session.getRemote().sendString(a);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

        );

    }

    public String get(Session session) {
        return users.get(session);
    }

    public void broadcastMessage(String sender, String message) throws JSONException {

        System.out.println(sender + " mówi " + message);


        users.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("message", message)

                ));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });


    }

    protected String createHtmlMessageFromSender(String sender, String message) {
        return article().with(
                b(sender + " napisał:"),
                p(message),
                span().withClass("timestamp").withText(new SimpleDateFormat("HH:mm:ss").format(new Date()))
        ).render();
    }

    public void remove(Session user) {
        String username = this.get(user);
        usersByName.remove(users.remove(user));
        try {
            this.broadcastMessage("Serwer", username + " opuścił kanał");
            this.toDelete(username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            user.getRemote().sendString(String.valueOf(new JSONObject().put("exit", "yes")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Map<Session, String> getUsers() {

        return users;
    }

    public void sendPrivateMessageTo(String sender, Session user, String message) {
        try {
            System.out.println(new JSONObject().put("message", message).put("sender", sender));
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat date= new SimpleDateFormat("HH:mm:ss");
            System.out.println(date.format(calendar.getTime()));
            user.getRemote().sendString(String.valueOf(new JSONObject().put("message", message).put("sender", sender).put("time", date.format(calendar.getTime()))));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public Session getSessionOf_(String user) {
        return this.usersByName.get(user);
    }

    public boolean has(String username) {
        return usersByName.containsKey(username);
    }
}

