import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import $ from 'jquery';

import {CookieService} from 'ngx-cookie-service';

const C_PATH_UNAME = 'om-uname';
const C_PATH_ROOM = 'om-room';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {


  constructor(private router: Router) {
  }

  private username: string;
  private roomId: string;

  ngOnInit() {
  }


  connect() {
    this.username = $('#nameCreate').val();
    this.roomId = $('#roomIDCreate').val();
    if (this.username && this.roomId) {
      this.chat(this.roomId, this.username);
    }
  }

  chat(roomId: string, uname: string) {
    this.router.navigate(['/chat', {roomid: roomId, userid: uname}]);
  }


}
