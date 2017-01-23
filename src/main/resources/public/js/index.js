var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat/");
webSocket.onmessage = function (msg) { updateChat(msg); };
webSocket.onclose = function () { alert("WebSocket connection closed") };
webSocket.onopen = function () {
    setLoginInput();
}

var actual
 function setLoginInput (){


     $( ".chat" ).remove();
    // $( ".container .right" ).append( "<h3>Podaj swoją nazwę</h3>" );
    // $( ".container .right" ).append( "<input id=\"nick\" placeholder=\"Podaj swój nick i wciśnij enter\">" );
     $( ".container .left .people" ).hide();
     $( "#nick" ).keypress(function(e) {
        if(e.which==13 && getCookie("channelname")!=""){
            saveInput();
            joinChannel(getCookie("channelname"), getCookie("nickname"))
            id("newchannel").value=getCookie("nickname")

            $( "#message" ).keypress(function(e) {

                if(e.which==13){

                    sendMessage(actual, id("message").value)
                    id("message").value="";



                }



            });



        }
     });
         $( "#newchannel" ).keypress(function(e) {
             if(e.which==13 && id("newchannel").value!=""){

                 addChannel();


             }


     });
 }





 function sendMessage(to, message){
     if(to!="Wszyscy")
     webSocket.send(JSON.stringify({
         pmessage: message,
         to: to

     }));
     else
         webSocket.send(JSON.stringify({
             message: message,
             to: to

         }));

     buildMessagelocal(message,to);




 }










 function updateChat(msg){
     var data = JSON.parse(msg.data);
     if(data.hasOwnProperty("channels")) {
         $( ".channels" ).empty();
         data.channels.forEach(function (channel) {
             addChannelToIndex(channel);
         });





         $('.left .channel').mousedown(function(){
             if ($(this).hasClass('.active')) {
                 return false;
             } else {
                 var findChat = $(this).attr('data-chat');
                 var channelName = $(this).find('.name').text();
                 $('.right .top .name').html(channelName);
                 $('.chat').removeClass('active-chat');
                 $('.left .person').removeClass('active');
                 $(this).addClass('active');
                 $('.chat[data-chat = '+findChat+']').addClass('active-chat');


                 setCookie("channelname", channelName,1);
                              }
         });



     }
     else{if(data.hasOwnProperty("users")){
         displayUsers(data)

         $('.left .person').mousedown(function(){
             if ($(this).hasClass('.active')) {
                 return false;
             } else {
                 var findChat = $(this).attr('data-chat');
                 var personName = $(this).find('.name').text();
                 $('.right .top .name').html(personName);
                 $('.chat').removeClass('active-chat');
                 $('.left .person').removeClass('active');
                 $(this).addClass('active');
                 $('.chat[data-chat = '+findChat+']').addClass('active-chat');
                // setCookie("toPerson", personName);
                 actual=personName;
               // window.alert("wysle do"+getCookie("toPerson"));

             }
         });

     }
     else {
         if (data.hasOwnProperty("message") && data.hasOwnProperty("sender")){

            // window.alert(data.message)
             buildMessage(data.message, data.sender, data.time)

         }
         else{
             if (data.hasOwnProperty("message") ){
                 buildMessage(data.message, "Wszyscy", "godzina")
             }
             if(data.hasOwnProperty("remove")){
                 deleteUser(data.remove)
             }
         }

     }

     }
 }



 function buildMessage(message, sender, time){


//window.alert(sender);


 var m="<div class=\"bubble you\">"+
        message+
     "</div>"




    // $('.chat[data-chat = '+findChat+']').addClass('active-chat');
     $('[data-chat= '+sender+']').find('.time').html(time.toString())
     $('[data-chat= '+sender+']').find('.preview').html(message.toString())
     insert(""+sender,m)
     var elem = document.getElementById("window");
     elem.scrollTop = elem.scrollHeight;





 }

 function buildMessagelocal(message, sender){


//window.alert(sender);


 var m="<div class=\"bubble me\">"+
        message+
     "</div>"

     insert(""+sender,m)
     var elem = document.getElementById("window");
     elem.scrollTop = elem.scrollHeight;




 }
function displayUsers(data){

    $( ".people" ).empty()
    $( ".people" ).append("<h3>Użytkownicy</h3>")
    $( ".people" ).append(buildAll());


    $( ".right .window" ).append("<div class=\"chat\" id=Wszyscy data-chat=Wszyscy></div>");
     data.users.forEach( function (user){
         $( ".people" ).append(buildUserHTML(user));

         $( ".right .window" ).append("<div class=\"chat\" id="+user+ " data-chat="+user+"></div>");





     }
     )


}



function deleteUser(user){
    $('.person[data-chat= '+user+']').remove();

}

function buildUserHTML(user){
    var res="<li class=\"person\" data-chat=\""+user+"\">" +
        "<img src=\"https://s13.postimg.org/ih41k9tqr/img1.jpg\" />" +
        "<span class=\"name\">"+user+"</span>"+
        "<span class=\"time\">0:00 AM</span>"+
    "<span class=\"preview\">ogown</span></li>"
    return res;
}
function buildAll(){
    var user="Wszyscy"
    var res="<li class=\"person\"  data-chat=\""+user+"\">" +
        "<img src=\"http://tarpley.net/images/ufaa.ico\" />" +
        "<span class=\"name\">"+user+"</span>"+
        "<span class=\"time\">0:00 AM</span>"+
        "<span class=\"preview\">asdas</span></li>"
    return res;
}

function addChannel(){
   var channel= id("newchannel").value
    webSocket.send(JSON.stringify({
        newChannel: channel
    }));
}

function addChannelToIndex(channel){

var inp="<li class=\"channel\" data-chat=\"channel\"> <span class=\"name\">"+channel+"</span> <span class=\"time\">users</span> </li>"


    $( ".channels" ).append( inp );






 }




function joinChannel(channel, nick){
    webSocket.send(JSON.stringify({
        nick: nick,
        channel: channel
    }));
}


 function saveInput(){

     setCookie("nickname", id("nick").value)
     $( ".chat" ).remove();
     $( "#nick" ).remove();

     $( ".container .left .people" ).show();
     $( ".container .left .channelscontrol" ).hide();

     //$( ".container .right" ).append( "<div class=\"top\"><span>Dołącz do: <span class=\"name\"></span></span></div>" );
     // $( ".container .right" ).append( "<div class=\"write\"> <input  id=\"message\" type=\"text\" /> <a href=\"javascript:;\" class=\"write-link smiley\"></a> <a href=\"javascript:;\" class=\"write-link send\"></a>   </div>" );
 }






function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("beforeend", message);
}


function id(id) {
    return document.getElementById(id);
}
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+ d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}
function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}