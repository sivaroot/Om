import { Component, ElementRef, OnDestroy, OnInit, Renderer2, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';


import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

import { CookieService } from 'ngx-cookie-service';
import { ChatService } from './chat.service';


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
  private serverUrl = 'http://localhost:8080/ws';
  isLoaded = false;
  messageInput: string;

  history: any;
  users: any;

  @ViewChild('inboxChat') inboxChat: ElementRef;
  @ViewChild('msgArea') msgArea: ElementRef;

  constructor(private route: ActivatedRoute,
    private cookie: CookieService,
    private renderer: Renderer2,
    private service: ChatService) {
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
    this.loadUsers(this.roomid);
  
  }

  initializeWebSocketConnection() {

    const ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null

    const that = this;
    // tslint:disable-next-line:only-arrow-functions
    this.stompClient.connect({}, function (frame) {
      // console.log('frame', frame);
      that.isLoaded = true;
      that.onConnected();
    });
    // this.stompClient.connect({}, that.onConnected(), that.onError());

  }

  onConnected() {
    if (this.currentSubscription) {
      this.currentSubscription.unsubscribe();
    }
    this.loadHistory(this.roomid);
    this.stompClient.subscribe(`/channel/${this.roomid}`, (payload) => {
      // console.log('payload', payload);
      this.handleResult(payload);
    });

    this.stompClient.send(`/app/chat/${this.roomid}/addUser`,
      {},
      JSON.stringify({ sender: this.userid, type: 'JOIN' })
    );

  }

  loadHistory(lobbyName: string) {
    this.service.getAllContentByLobby(lobbyName).subscribe(res => {
      this.history = res;
      // console.log(this.history);
    });
  }

  loadUsers(lobbyName: string) {
    this.service.getAllUsersByLobby(lobbyName).subscribe(res => {
      this.users = res;
      this.users.reverse();
    })
    setTimeout(() => {
      this.loadUsers(this.roomid)
    }, 10000);
  }

  handleResult(payload) {

    if (payload.body) {
      // console.log('message', payload);
      const message = JSON.parse(payload.body);

      const callElement = this.renderer.createElement('div');
      this.loadUsers(this.roomid);

      if (message.type === 'CHAT') {

        if (message.sender === this.userid) {
          this.renderer.addClass(callElement, 'outgoing_msg');

          const sentElement = this.renderer.createElement('div');
          this.renderer.addClass(sentElement, 'sent_msg');

          const contentElement = this.renderer.createElement('p');

          contentElement.innerHTML = message.content;
          const spanElement = this.renderer.createElement('span');
          this.renderer.addClass(spanElement, 'sent_msg_span');
          spanElement.innerHTML = this.formattime(message.messageTime) + ' Send by me';

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
          spanElement.innerHTML = this.formattime(message.messageTime) + ' Send by ' + message.sender;
          this.renderer.addClass(spanElement, 'callDetailIn');

          this.renderer.appendChild(receivedElement, contentElement);
          this.renderer.appendChild(receivedElement, spanElement);
          this.renderer.appendChild(callElement, receivedElement);
        }

      } else if (message.type === 'JOIN') {
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

  formattime(timestamp: any): string {
    timestamp = timestamp.split('T');
    const date = timestamp[0];
    const time = timestamp[1];
    const timeFormat = time.split(':')[0] + ':' + time.split(':')[1];
    return date + ' ' + timeFormat;
  }


  ngOnDestroy(): void {
    this.currentSubscription.unsubscribe();
  }

}
