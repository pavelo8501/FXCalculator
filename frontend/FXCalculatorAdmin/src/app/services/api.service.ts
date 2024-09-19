import { Injectable } from '@angular/core';
import { environment } from '../../environment/environment.prod';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';
import { IFeesResponse } from '../classes/get/IFeesResponse';
import { IRate } from '../classes/rates/IRate';
import { IRatesResponse } from '../classes/get/IRatesResponse';
import { IFee } from '../classes/fees/IFee';
import { IResultResponse } from '../classes/get/IResultResponse';
import { json } from 'stream/consumers';

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

  updateFee(iFee: IFee){
    let method = `fees/update/${iFee.id}`;
   // let bodyContent = JSON.stringify(iFee.fee);
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.patch<IResultResponse>(this._request_url+method,iFee.fee,{headers});
  }

  getRates():Observable<IRatesResponse>{
    let method = "rates"
    return this.http.get<IRatesResponse>(this._request_url+method);
  }

  deleteFee(iFee:IFee){
    let method = `fees/delete/${iFee.id}`;
    return this.http.delete<IResultResponse>(this._request_url+method);
  }

}
