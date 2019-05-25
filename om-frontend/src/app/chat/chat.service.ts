import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';


const HOST = 'http://localhost:8080/';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  constructor(private http: HttpClient) {
  }

  getAllContentByLobby(lobbyname: string): Observable<any> {
    return this.http.get(HOST + 'content/' + lobbyname);
  }
  getAllUsersByLobby(lobbyname: string): Observable<any> {
    return this.http.get(HOST + 'users/' + lobbyname);
  }
}
