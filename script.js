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

const name = prompt("What's your name?");

if(!location.hash){
	location.hash = Math.floor(Math.random() * 0xFFFFFF).toSTring(16);
}
const chatHash = location.hash.substring(1);

const configuration = {
	iceServers:[{
		url: 'stun:stun.l.google.com:19302'
	}]
};

//RTCPeerConnection
let pc;
let dataChannel;


//channel settings for signal server
const drone = new ScaleDrone('rm165CYauSDS4hzU');

//scaledrone room names specifically need to be prefixed with 'observable-'
const roomName = 'observable-' + chatHash;

//Scaledrone room used for signaling
let room;

//Wait for scaledrone signaling server to connect
drone.on('open', error => {
	if(error){
		return console.error(error);
	}
	room = drone.subscribe(roomName);
	room.on('open', error => {
		if(error){
			return console.error(error);
		}
		console.log('Connected to signaling server');
	});
	//We're now connected to the room and receive an array of
	//other users connected to the room, including us. Signaling server
	// is ready.
	room.on('members', members => {
		if(members.length >= 3){
			return alert('The room is full');
		}
		const isOfferer = members.length === 2;
		startWebRTC(isOfferer);
	});
});


/**
 * sendSignalingMessage()
 * used to send a signaling message to devices connected to the Scaledrone room
 */
function sendSignalingMessage(message){
	drone.publish({
		room: roomName,
		message
	});
}

/**
 * startWebRTC
 * This is used whenever a user is connecting that is the offerer,
 * creates the RTCDataChannel for the room to use
 */
function startWebRTC(isOfferer) {
  console.log('Starting WebRTC in as', isOfferer ? 'offerer' : 'waiter');
  pc = new RTCPeerConnection(configuration);
 
  // 'onicecandidate' notifies us whenever an ICE agent needs to deliver a
  // message to the other peer through the signaling server
  pc.onicecandidate = event => {
    if (event.candidate) {
      sendSignalingMessage({'candidate': event.candidate});
    }
  };
 
 
  if (isOfferer) {
    // If user is offerer let them create a negotiation offer and set up the data channel
    pc.onnegotiationneeded = () => {
      pc.createOffer(localDescCreated, error => console.error(error));
    }
    dataChannel = pc.createDataChannel('chat');
    setupDataChannel();
  } else {
    // If user is not the offerer let wait for a data channel
    pc.ondatachannel = event => {
      dataChannel = event.channel;
      setupDataChannel();
    }
  }
 
  startListentingToSignals();

}

// Hook up data channel event handlers
function setupDataChannel() {
  checkDataChannelState();
  dataChannel.onopen = checkDataChannelState;
  dataChannel.onclose = checkDataChannelState;
  dataChannel.onmessage = event =>
    insertMessageToDOM(JSON.parse(event.data), false)
}
 
function checkDataChannelState() {
  console.log('WebRTC channel state is:', dataChannel.readyState);
  if (dataChannel.readyState === 'open') {
    insertMessageToDOM({content: 'WebRTC data channel is now open'});
  }
}


function startListentingToSignals() {
  // Listen to signaling data from Scaledrone
  room.on('data', (message, client) => {
    // Message was sent by us
    if (client.id === drone.clientId) {
      return;
    }
    if (message.sdp) {
      // This is called after receiving an offer or answer from another peer
      pc.setRemoteDescription(new RTCSessionDescription(message.sdp), () => {
        console.log('pc.remoteDescription.type', pc.remoteDescription.type);
        // When receiving an offer lets answer it
        if (pc.remoteDescription.type === 'offer') {
          console.log('Answering offer');
          pc.createAnswer(localDescCreated, error => console.error(error));
        }
      }, error => console.error(error));
    } else if (message.candidate) {
      // Add the new ICE candidate to our connections remote description
      pc.addIceCandidate(new RTCIceCandidate(message.candidate));
    }
  });
}

function localDescCreated(desc) {
  pc.setLocalDescription(
    desc,
    () => sendSignalingMessage({'sdp': pc.localDescription}),
    error => console.error(error)
  );
}

function insertMessageToDOM(options, isFromMe) {
  const template = document.querySelector('template[data-template="message"]');
  const nameEl = template.content.querySelector('.message__name');
  if (options.emoji || options.name) {
    nameEl.innerText = options.emoji + ' ' + options.name;
  }
  template.content.querySelector('.message__bubble').innerText = options.content;
  const clone = document.importNode(template.content, true);
  const messageEl = clone.querySelector('.message');
  if (isFromMe) {
    messageEl.classList.add('message--mine');
  } else {
    messageEl.classList.add('message--theirs');
  }
 
  const messagesEl = document.querySelector('.messages');
  messagesEl.appendChild(clone);
 
  // Scroll to bottom
  messagesEl.scrollTop = messagesEl.scrollHeight - messagesEl.clientHeight;
}

const form = document.querySelector('form');
form.addEventListener('submit', () => {
  const input = document.querySelector('input[type="text"]');
  const value = input.value;
  input.value = '';
 
  const data = {
    name,
    content: value,
    emoji,
  };
 
  dataChannel.send(JSON.stringify(data));
 
  insertMessageToDOM(data, true);
});
 
insertMessageToDOM({content: 'Chat URL is ' + location.href});