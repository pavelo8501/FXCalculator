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
import { IFeeRequest } from '../classes/requests/IFeeRequest';
import { IFeeResponse } from '../classes/get/IFeeResponse';

@Injectable({
  providedIn: 'root'
})

export class ApiService {

  private _request_url : string = environment.api_url;

  constructor(private http:HttpClient) { }

  headers = new HttpHeaders({ 'Content-Type': 'application/json', 'X-API-VERSION': '1'});

  getRates():Observable<IRatesResponse>{
    let method = "rates"
    const headers = new HttpHeaders({ 'Content-Type': 'application/json', 'X-API-VERSION': '1'});
    return this.http.get<IRatesResponse>(this._request_url+method,{"headers" :this.headers});
  }

  updateRates(){
    let method = "rates"
    return this.http.post<IRatesResponse>(this._request_url+method,{},{"headers" :this.headers});
  }

  getFees():Observable<IFeesResponse> {
    let method = "fees"
    return this.http.get<IFeesResponse>(this._request_url+method,{"headers" :this.headers});
  }

  updateFee(iFee: IFee){
    let method = `fees/${iFee.id}`;
   // let bodyContent = JSON.stringify(iFee.fee);
    return this.http.patch<IResultResponse>(this._request_url+method,iFee.fee,{"headers" :this.headers});
  }

  saveFee(iFee: IFee){
    let method = `fees`;
    let request : IFeeRequest = {fromCurrencyId:iFee.fromCurrency.id, toCurrencyId:iFee.toCurrency.id,fee:iFee.fee}
    const headers = new HttpHeaders({ 'Content-Type':'application/json', 'X-API-VERSION':'1'});
    return this.http.post<IFeeResponse>(this._request_url+method,request,{headers});
  }

  deleteFee(iFee:IFee){
    let method = `fees/${iFee.id}`;
    return this.http.delete<IResultResponse>(this._request_url+method,{"headers" :this.headers});
  }

}
