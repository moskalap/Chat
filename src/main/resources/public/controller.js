/**
 * Created by przemek on 15.01.17.
 */

var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat/");
webSocket.onmessage = function (msg) { updateChat(msg); };
webSocket.onclose = function () { alert("WebSocket connection closed") };


id("nick").addEventListener("keypress", function (e){
    if(e.keyCode==13){
        id("userName").style.visibility="hidden";
        id("channelsControl").style.visibility="visible";

    }
});


id("newChatName").addEventListener("keypress", function(e){
    if(e.keyCode==13){


        addChannel(id("newChatName").value)
        joinChannel(id("nick").value,id("newChatName").value);

        id("newChatName").style.visibility="hidden";
        id("channelComponents").style.visibility="visible";
        id("channelsControl").style.visibility="hidden";


        id(id("newChatName").value).addEventListener("click", function () {

            id("channelsControl").style.visibility="hidden";


        });






    }
});


id("message").addEventListener("keypress", function (e){
    if(e.keyCode==13){
        sendMessage(id("message").value);

    }
});

function joinChannel(id2, channel) {


   //id("title").innerHTML=id2+"@"+channel
    webSocket.send(JSON.stringify({
        nick: id2,
        channel: channel
    }));

}
function addChannel(channelName){
    webSocket.send(JSON.stringify({
       channelName: channelName
    }));
}

function sendMessage(message){
    webSocket.send(JSON.stringify({
            message: message
        }
    ));
    id("message").value="";


}


function updateChat(msg) {

    var data = JSON.parse(msg.data);
    if(data.hasOwnProperty("channels")){

        data.channels.forEach( function (channel) {
            addChannelToIndex(channel);
            id(channel).addEventListener("click", function () {
                joinChannel(id("nick").value, channel);
                id("newChatName").style.visibility="hidden";
                id("channelComponents").style.visibility="visible";
                id("channelsControl").style.visibility="hidden";
            });
        });

    }else{
        if (data.hasOwnProperty("users")){
            //window.alert("Dostałęm liste");
            id("users").innerHTML = "";
            data.users.forEach( function (user){
                insert("users", "<li id=\"U"+user+"\"> "+user+ "</li>");
                id("U"+user).addEventListener("click", function () {
                    //window.alert(user);
                    id("privates").innerHTML= "";
                    insert("privates", "<input id=\"pmessage\" placeholder=\"Napisz wiadomość...\">");
                    insert("privates", "<h3> Napisz wiadomość do "+user +"</h3>");
                    id("pmessage").addEventListener("keypress", function (e){
                        if(e.keyCode==13){
                            sendPrivateMessage(user, id("pmessage").value);
                            id("privates").innerHTML= "";

                        }
                    })






                });

                //    insert("users", "<li>" + user + "</li>");
                }
            );
        }
        else
        {
            if(data.hasOwnProperty("message")){
               // window.alert("asdsd");
                insert("chat", data.message);
            }
        }
    }



}







function addChannelToIndex(channel){
    insert("channels", "<li id=\""+channel+"\"> "+channel+ "</li>");

}

function sendPrivateMessage(to, message){
    webSocket.send(JSON.stringify({
            pmessage: message,
            to: to
        }
    ));

}

function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}


function id(id) {
    return document.getElementById(id);
}
