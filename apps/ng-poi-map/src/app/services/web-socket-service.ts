import { Injectable } from '@angular/core';
import { delayWhen, Observable, retryWhen, Subject, tap, timer } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  private subject$!: Subject<MessageEvent>;
  
  constructor() { }

  public connect(url:string): Subject<MessageEvent> {
    if (!this.subject$) {
      this.subject$ = this.create(url);
    }
    return this.subject$;
  }

  private create(url: string): Subject<MessageEvent<any>> {
    
    let webSocket:WebSocket;
    
    let observable = new Observable<MessageEvent>(observer => {
      webSocket = new WebSocket(url);
      webSocket.onopen = () => console.log('🔌 websocket connected at '+url+' 🤩');
      webSocket.onerror = observer.error.bind(observer);
      webSocket.onclose = observer.error.bind(observer);
      webSocket.onmessage = observer.next.bind(observer);
      return webSocket.close.bind(webSocket);
    }).pipe(
      retryWhen(errors => {
      return errors.pipe(
        tap(() => {console.log('🚨 upps... websocket connection error 😭');}),
        delayWhen(() =>  {
            console.log('⏱️ backing off for a while');      
            return timer(5000).pipe(tap(() => {
              console.log('🔁 trying to reconnect websocket');
            }));
          }
        )
      );
      }
    ));
    
    let observer = {
      next: (data: Object) => {
        console.log('🧙🏻‍♂️ YOU SHALL NOT PASS! the server does not expect data 😬');
      }
    };
    
    return Subject.create(observer,observable);
  }
}
