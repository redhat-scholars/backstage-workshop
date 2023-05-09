import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { PoiRecord } from '../models/custom-types';
import { environment as env } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class GatewayService {
  
  constructor(private http: HttpClient) {}

  getAll(backendId: string): Observable<PoiRecord[]> {
    const headers = new HttpHeaders().set('Accept', 'application/json');
    const params = new HttpParams().set('service', backendId);
    return this.http.get<PoiRecord[]>(env.gatewayApiUrl+'/ws/data/all', { headers, params });
  }
}
