var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat/");
webSocket.onmessage = function (msg) { updateChat(msg); };
webSocket.onclose = function () { alert("WebSocket connection closed") };
webSocket.onopen = function () {
    setLoginInput();
    $( "#write" ).hide();
    $('.right .top').hide();

}
var boolChosenCh=false;
var readedNick=false;
var actual
var nickname;
function setLoginInput (){


     $( ".chat" ).remove();
     $( ".container .left .people" ).hide();
     $( ".container .left .channelscontrol" ).hide();

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

            addChEventListeners()







     }
     else{if(data.hasOwnProperty("users")){
         displayUsers(data)
         addEventListeners();


     }
     else {
         if (data.hasOwnProperty("message") && data.hasOwnProperty("sender")){

            buildMessage(data.message, data.sender, data.time)

         }
         else{
             if (data.hasOwnProperty("message") ){
                 buildMessageALL(data.message, data.broadcaster, new Date(new Date().getTime()).toLocaleTimeString())
             }
             if(data.hasOwnProperty("remove")){
                 deleteUser(data.remove);
             }
             if(data.hasOwnProperty("add")){
                 addUserToHTML(data.add);
                 addEventListeners();


             }
         }

     }

     }
 }
function buildMessage(message, sender, time){





         var m = "<div class=\"bubble you\">" +
             message +
             "</div>"



         $('[data-chat= ' + sender + ']').find('.time').html(time.toString())
         $('[data-chat= ' + sender + ']').find('.preview').html(message.toString())
         insert("" + sender, m)
         var elem = document.getElementById("window");
         elem.scrollTop = elem.scrollHeight;




 }
function buildMessageALL(message, sender, time){
if(sender!=nickname) {
    s = "Wszyscy"



    var m = "<div class=\"bubble all\">" +
        "<div id=\"group\">" + sender + " napisał:</div>" +
        message +
        "</div>"


    // $('.chat[data-chat = '+findChat+']').addClass('active-chat');
    $('[data-chat= ' + s + ']').find('.time').html(time.toString())
    $('[data-chat= ' + s + ']').find('.preview').html(message.toString())
    insert("" + s, m)
    var elem = document.getElementById("window");
    elem.scrollTop = elem.scrollHeight;


}

 }
function buildMessagelocal(message, sender){





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
        addUserToHTML(user);






     }
     )


}
function addChEventListeners() {
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

            boolChosenCh=true;
            setCookie("channelname", channelName,1);
            saveInput();

        }
    });
}
function addUserToHTML(user){
    $( ".people" ).append(buildUserHTML(user));
    $( ".right .window" ).append("<div class=\"chat\" id="+user+ " data-chat="+user+"></div>");

}
function deleteUser(user){
    $('.person[data-chat= '+user+']').remove();

}
function addEventListeners(){
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
            actual=findChat;

            $( "#write" ).show();
            $('.right .top').show();

        }
    });
}
function buildUserHTML(user){

var d = new Date(); // for now
d.getHours(); // => 9
d.getMinutes(); // =>  30
d.getSeconds(); // => 51
    var username=user;
if (user==nickname) {username ="Ty("+user+")" }

    var res="<li class=\"person\" data-chat=\""+user+"\">" +
        "<img src=\"http://icons.iconarchive.com/icons/graphicloads/flat-finance/48/person-icon.png\" />" +
        "<span class=\"name\">"+username+"</span>"+
        "<span class=\"time\">"+new Date(new Date().getTime()).toLocaleTimeString()+"</span>"+
    "<span class=\"preview\">Nowy</span></li>"
    return res;
}
function buildAll(){
    var user="Wszyscy"
    var res="<li class=\"person\"  data-chat=\""+user+"\">" +
        "<img src=\"http://tarpley.net/images/ufaa.ico\" />" +
        "<span class=\"name\">"+user+"</span>"+
        "<span class=\"time\"></span>"+
        "<span class=\"preview\"></span></li>"
    return res;
}
function addChannel(){
   var channel= id("newchannel").value
    webSocket.send(JSON.stringify({
        newChannel: channel
    }));
}
function addChannelToIndex(channel){

var inp="<li class=\"channel\" data-chat=\"channel\"> <span class=\"name\">"+channel+"</span> <span class=\"time\"></span> </li>"


    $( ".channels" ).append( inp );






 }
function joinChannel(channel, nick){
    webSocket.send(JSON.stringify({
        nick: nick,
        channel: channel
    }));
}
function saveNick(){
    setCookie("nickname", id("nick").value, 1);
    nickname=id("nick").value;
    $( "#but" ).attr( "class", "join" );
    $( "#but" ).attr( "href", "javascript:saveInput()" );
    $( ".container .left .channelscontrol" ).show();
    $( "#nick" ).remove();
    $( "#message" ).keypress(function(e) {

        if(e.which==13){

            sendMessage(actual, id("message").value)
            id("message").value="";



        }



    });


}
function saveInput(){


     if(!boolChosenCh) {
         window.alert("Musisz wybrać kanał!")

     }
     else {
         $( "#but" ).attr( "class", "leave" );
         $( "#but" ).attr( "href", "javascript:leave()" );

         $( ".chat" ).remove();
         $( ".container .left .people" ).show();
         $( ".container .left .channelscontrol" ).hide();



         joinChannel(getCookie("channelname"), nickname)
        // id("newchannel").value=getCookie("nickname")



     }


     //$( ".container .right" ).append( "<div class=\"top\"><span>Dołącz do: <span class=\"name\"></span></span></div>" );
     // $( ".container .right" ).append( "<div class=\"write\"> <input  id=\"message\" type=\"text\" /> <a href=\"javascript:;\" class=\"write-link smiley\"></a> <a href=\"javascript:;\" class=\"write-link send\"></a>   </div>" );
 }
function leave(){
    webSocket.send(JSON.stringify({
        exit: "y"
    }));
setLoginInput();
    $( ".chat" ).remove();
    $( "#nick" ).remove();

    $( ".container .left .people" ).hide();
    $( ".container .left .channelscontrol" ).show();
    $( "#but" ).attr( "class", "join" );
    $( "#but" ).attr( "href", "javascript:saveInput()" );



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