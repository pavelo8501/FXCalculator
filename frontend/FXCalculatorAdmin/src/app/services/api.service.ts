import { Injectable } from '@angular/core';
import { environment } from '../../environment/environment.prod';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';
import { IFeesResponse } from '../classes/get/IFeesResponse';
import { IRate } from '../classes/rates/IRate';
import { IRatesResponse } from '../classes/get/IRatesResponse';

@Injectable({
  providedIn: 'root'
})

export class ApiService {

  private _request_url : string = environment.api_url;

  constructor(private http:HttpClient) { }

  getFees():Observable<IFeesResponse> {
    let method = "fees"
    return this.http.get<IFeesResponse>(this._request_url+method);
  }

  getRates():Observable<IRatesResponse>{
    let method = "rates"
    return this.http.get<IRatesResponse>(this._request_url+method);
  }

}
