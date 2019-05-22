import {Component, OnDestroy, OnInit, Renderer2, ViewChild, ElementRef} from '@angular/core';
import {ActivatedRoute} from '@angular/router';


import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

import {CookieService} from 'ngx-cookie-service';


const C_PATH_UNAME = 'om-uname';
const C_PATH_ROOM = 'om-room';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy {

  roomid;
  private userid;
  private sub: any;
  private currentSubscription = null;
  private stompClient;
  private serverUrl = 'http://35.239.193.180:8080/ws';
  isLoaded = false;
  messageInput: string;

  @ViewChild('inboxChat') inboxChat: ElementRef;
  @ViewChild('msgArea') msgArea: ElementRef;

  constructor(private route: ActivatedRoute,
              private cookie: CookieService,
              private renderer: Renderer2) {
  }

  ngOnInit() {
    this.sub = this.route.params.forEach((urlParams) => {
      this.userid = urlParams.userid.trim();
      this.roomid = urlParams.roomid;
    });
    console.log(this.userid, this.roomid);
    this.cookie.set(C_PATH_UNAME, this.userid);
    this.cookie.set(C_PATH_ROOM, this.roomid);
    this.initializeWebSocketConnection();
  }

  initializeWebSocketConnection() {

    const ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    const that = this;
    // tslint:disable-next-line:only-arrow-functions
    this.stompClient.connect({}, function(frame) {
      console.log('frame', frame);
      that.isLoaded = true;
      that.onConnected();
    });
    // this.stompClient.connect({}, that.onConnected(), that.onError());

  }

  onConnected() {
    if (this.currentSubscription) {
      this.currentSubscription.unsubscribe();
    }
    this.stompClient.subscribe(`/channel/${this.roomid}`, (payload) => {
      console.log('payload', payload);
      this.handleResult(payload);
    });

    this.stompClient.send(`/app/chat/${this.roomid}/addUser`,
      {},
      JSON.stringify({sender: this.userid, type: 'JOIN'})
    );

  }

  onError(error) {

  }

  openGlobalSocket() {
    this.stompClient.subscribe(`/channel/${this.roomid}`, (payload) => {
      this.handleResult(payload);
    });
  }

  handleResult(payload) {

    if (payload.body) {
      console.log('message', payload);
      const message = JSON.parse(payload.body);

      const callElement = this.renderer.createElement('div');

      if (message.type === 'CHAT') {

        if (message.sender === this.userid) {
          this.renderer.addClass(callElement, 'outgoing_msg');

          const sentElement = this.renderer.createElement('div');
          this.renderer.addClass(sentElement, 'sent_msg');

          const contentElement = this.renderer.createElement('p');

          contentElement.innerHTML = message.content;
          const spanElement = this.renderer.createElement('span');
          this.renderer.addClass(spanElement, 'sent_msg_span');
          spanElement.innerHTML = 'Send by me';

          this.renderer.addClass(spanElement, 'callDetailOut');
          this.renderer.appendChild(sentElement, contentElement);
          this.renderer.appendChild(sentElement, spanElement);
          this.renderer.appendChild(callElement, sentElement);
        } else {
          this.renderer.addClass(callElement, 'incoming_msg');
          const receivedElement = this.renderer.createElement('div');
          this.renderer.addClass(receivedElement, 'received_msg');
          const contentElement = this.renderer.createElement('p');
          contentElement.innerHTML = message.content;
          const spanElement = this.renderer.createElement('span');
          spanElement.innerHTML = 'Send by ' + message.sender;
          this.renderer.addClass(spanElement, 'callDetailIn');

          this.renderer.appendChild(receivedElement, contentElement);
          this.renderer.appendChild(receivedElement, spanElement);
          this.renderer.appendChild(callElement, receivedElement);
        }

      } else if (message.type === 'JOIN') {
        this.inboxPeople(payload);


        this.renderer.addClass(callElement, 'join_msg');
        const contentElement = this.renderer.createElement('p');
        contentElement.innerHTML = message.sender + ' joined!';
        this.renderer.appendChild(callElement, contentElement);


      } else if (message.type === 'LEAVE') {
        this.renderer.addClass(callElement, 'join_msg');
        const contentElement = this.renderer.createElement('p');
        contentElement.innerHTML = message.sender + ' left!';
        this.renderer.appendChild(callElement, contentElement);
      }
      this.renderer.appendChild(this.msgArea.nativeElement, callElement);

      this.msgArea.nativeElement.scrollTop = this.msgArea.nativeElement.scrollHeight;
    }
  }

  sendMessage() {
    console.log(event);
    // tslint:disable-next-line:prefer-const
    let messageContent = this.messageInput;
    if (messageContent && this.stompClient) {
      const chatMessage = {
        sender: this.userid,
        content: messageContent,
        type: 'CHAT'
      };
      this.stompClient.send(`/app/chat/${this.roomid}/sendMessage`, {}, JSON.stringify(chatMessage));
    }
    this.messageInput = '';

  }

  inboxPeople(payload) {
    const message = JSON.parse(payload.body);

    const chatList = this.renderer.createElement('div');
    this.renderer.addClass(chatList, 'chat_list');

    const chatPeople = this.renderer.createElement('div');
    this.renderer.addClass(chatPeople, 'chat_people');

    const chatImg = this.renderer.createElement('div');
    this.renderer.addClass(chatImg, 'chat_img');

    const img = this.renderer.createElement('img');
    this.renderer.setProperty(img, 'src', 'https://ptetutorials.com/images/user-profile.png');
    this.renderer.setProperty(img, 'alt', 'sunil');

    const chatIb = this.renderer.createElement('div');
    this.renderer.addClass(chatIb, 'chat_ib');
    chatIb.innerHTML = message.sender;

    this.renderer.appendChild(chatImg, img);
    this.renderer.appendChild(chatPeople, chatImg);
    this.renderer.appendChild(chatPeople, chatIb);
    this.renderer.appendChild(chatList, chatPeople);
    this.renderer.appendChild(this.inboxChat.nativeElement, chatList);
  }


  ngOnDestroy(): void {
    this.currentSubscription.unsubscribe();
  }

}
