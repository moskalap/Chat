import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by przemek on 16.01.17.
 */
@WebSocket
public class ChatWebSocketHandler {
    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        System.out.println("połączono");
        user.getRemote().sendString(String.valueOf(new JSONObject()
                .put("channels", Chat.channels.keySet())
        ));

    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        Channel channel = Chat.channelOf_(user);
        channel.broadcastMessage("Serwer", channel.get(user) + " opuścił czat.");
        channel.remove(user);
        channel.sendUsersList();
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.print(message);

        JSONObject json;
        try {
            json = new JSONObject(message);

        if (json.has("nick")) {
            System.out.println(message);
            String username = null;
                username = json.getString("nick");
                String channelName = null;
                channelName = json.getString("channel");
                System.out.println(channelName);
                System.out.println(username);


            if (Chat.channels.containsKey(channelName)) {
                Chat.addUser(username, user, channelName);

            } else {
                Chat.addChannel(channelName);
                Chat.addUser(username, user, channelName);

            }
            //Wysyłamy liste  userów dla każdego websocketa z kanału
            Chat.channels.get(channelName).sendUsersList();
            Chat.channels.get(channelName).broadcastMessage(username, username + " joined the chat.");
        } else {
            if (json.has("message")) {
                System.out.println(Chat.channelOf_(user).getName());
                if(Bot.findsTaskIn(json.getString("message"))) {
                    Chat.channelOf_(user).sendPrivateMessageTo("ChatBot", user, Bot.executeTask(json.getString("message")));
                } else
                    Chat.channelOf_(user).broadcastMessage(Chat.channelOf_(user).get(user), json.getString("message"));


            }
            else{
                if(json.has("pmessage")){

                    Chat.channelOf_(user).sendPrivateMessageTo(
                            Chat.channelOf_(user).getName(),
                            Chat.channelOf_(user).getSessionOf_(json.getString("to")),
                            json.getString("pmessage")
                    );
                }
            }

        }



    }
     catch (JSONException e) {
        e.printStackTrace();
    }


}
}

