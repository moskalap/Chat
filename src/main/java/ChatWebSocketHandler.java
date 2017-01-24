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
    private Chat chat;


    public ChatWebSocketHandler(Chat chat){
        this.chat=chat;

    }


    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        System.out.println("połączono");
        System.out.println(String.valueOf(new JSONObject()
                .put("channels", chat.getChannels().keySet())));
        user.getRemote().sendString(String.valueOf(new JSONObject()
                .put("channels", chat.getChannels().keySet())
        ));

    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        Channel channel = chat.channelOf_(user);
        try {
            channel.broadcastMessage("Serwer", channel.get(user) + " opuścił czat.");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        channel.remove(user);
        chat.deleteUser(user);

    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.println(message);

        JSONObject json;
        try {
            json = new JSONObject(message);

        if (json.has("nick")) {
            //System.out.println(message);
            String username = null;
                username = json.getString("nick");
                String channelName = null;
                channelName = json.getString("channel");
                //System.out.println(channelName);
                //System.out.println(username);

            chat.getChannels().get(channelName).sendnameOfUser(username);
            if (chat.getChannels().containsKey(channelName)) {
                chat.addUser(username, user, channelName);

            } else {
                chat.addChannel(channelName);
                chat.addUser(username, user, channelName);

            }
            //Wysyłamy liste  userów dla każdego websocketa z kanału
            chat.getChannels().get(channelName).sendUsersList(user);

            chat.getChannels().get(channelName).broadcastMessage(username, username + " joined the chat.");
        } else {
            if(json.has("newChannel")){
                chat.addChannel(json.getString("newChannel"));
                try {
                    user.getRemote().sendString(String.valueOf(new JSONObject()
                            .put("channels", chat.getChannels().keySet())
                    ));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (json.has("message")) {
                System.out.println(chat.channelOf_(user).getName());

                    chat.channelOf_(user).broadcastMessage(chat.channelOf_(user).get(user), json.getString("message"));


            }
            else{
                if(json.has("pmessage")){

                    chat.channelOf_(user).sendPrivateMessageTo(
                            chat.channelOf_(user).getUsers().get(user),
                            chat.channelOf_(user).getSessionOf_(json.getString("to")),
                            json.getString("pmessage")
                    );
                }else{
                    if (json.has("exit")){
                        chat.channelOf_(user).remove(user);
                        chat.deleteUser(user);
                    }
                }
            }

        }



    }
     catch (JSONException e) {
        e.printStackTrace();
    }


}
}

