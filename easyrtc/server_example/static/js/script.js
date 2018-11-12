const possibleEmojis = [
  '🐀','🐁','🐭','🐹','🐂','🐃','🐄','🐮','🐅','🐆','🐯','🐇','🐐','🐑','🐏','🐴',
  '🐎','🐱','🐈','🐰','🐓','🐔','🐤','🐣','🐥','🐦','🐧','🐘','🐩','🐕','🐷','🐖',
  '🐗','🐫','🐪','🐶','🐺','🐻','🐨','🐼','🐵','🙈','🙉','🙊','🐒','🐉','🐲','🐊',
  '🐍','🐢','🐸','🐋','🐳','🐬','🐙','🐟','🐠','🐡','🐚','🐌','🐛','🐜','🐝','🐞',
];

//This function returns a random emoji to assign to a new client entry
function randomEmoji(){
	const randomIndex = Math.floor(Math.random() * possibleEmojis.length);
	return possibleEmojis[randomIndex];
}

const emoji = randomEmoji();
const form = document.querySelector('form');
form.addEventListener('submit', () => {
    const userSelector = document.getElementById("userSelect");
    const userId = userSelector.options[userSelector.selectedIndex].value;
});

if(!location.hash){
	location.hash = Math.floor(Math.random() * 0xFFFFFF).toString(16);
}
const chatHash = location.hash.substring(1);

function addToConversation(who, msgType, content){
  //Need to escape any special characters
  content = content.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
  content = content.replace(/\n/g, '<br />');
  const template = document.querySelector('template[data-template="message"]');
  const nameEl = template.content.querySelector('.message__name');
  nameEl.innerText = who;
  template.content.querySelector('.message__bubble').innerText = content;
  const clone = document.importNode(template.content, true);
  const messageEl = clone.querySelector('.message');
  messageEl.classList.add('message--theirs');
}

function connect(){
  easyrtc.setPeerListener(addToConversation);
  easyrtc.setRoomOccupantListener(convertListToIcons);
  easyrtc.connect("easyrtc.instantMessaging", loginSuccess, loginFailure);

}

function convertListToIcons(roomName, occupants, isPrimary){
  var otherClientDiv = document.getElementById('otherUsers');
  while(otherClientDiv.hasChildNodes()){
      otherClientDiv.removeChild(otherClientDiv.lastChild);
    }
    const userSelector = document.getElementById("userSelect");
    for (var easyrtcid in occupants) {
        userSelector.options[userSelector.options.length] = new Option(easyrtc.idToName(easyrtcid), easyrtcid);
  }

}

function sendStuffWS(otherEasyrtcid) {
    var text = document.getElementById('sendMessageText').value;
    if (text.replace(/\s/g, "").length === 0) { // Don't send just whitespace
        return;
    }

    easyrtc.sendDataWS(otherEasyrtcid, "message", text);
    addToConversation("Me", "message", text);
    document.getElementById('sendMessageText').value = "";
}

function loginSuccess(easyrtcid) {
    selfEasyrtcid = easyrtcid;
    document.getElementById("iam").innerHTML = "I am " + easyrtcid;
}


function loginFailure(errorCode, message) {
    easyrtc.showError(errorCode, message);
}

