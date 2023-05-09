import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { BackendChangeEvent } from '../models/custom-types';
import { WebSocketService } from './web-socket-service';
import { environment as env } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BackendRegistryService {

  public messages$! : Observable<BackendChangeEvent>;

  constructor(wsService: WebSocketService) {
    this.messages$ = wsService
      .connect(env.websocketEndpoint).pipe(
        map((response: MessageEvent): BackendChangeEvent => {
            return JSON.parse(response.data) as BackendChangeEvent;
      })
    );
  }
}
