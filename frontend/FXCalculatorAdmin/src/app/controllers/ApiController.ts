import { BehaviorSubject, Observable } from "rxjs";
import { Fee } from "../classes/fees/Fee";
import { Rate } from "../classes/rates/Rate";
import { ApiService } from "../services/api.service";
import { Injectable } from "@angular/core";
import { IFee } from "../classes/fees/IFee";
import { IRate } from "../classes/rates/IRate";
import { response } from "express";


@Injectable({
    providedIn: 'root'
})

export class ApiController{

    constructor(private apiService: ApiService) {
        
    }

    fees: Fee[] = []

    private feesSubject = new BehaviorSubject<Fee[]>(this.fees);
    public getFees():Observable<Fee[]> {
        this.apiService.getFees().subscribe({
            next:(response)=>{
                if(response.ok == true){
                    let list = (response.result as  IFee[]).map(x=>{
                        return new Fee(
                            x.id,
                            new Rate(x.id, x.fromCurrency.currency, x.fromCurrency.rate),
                            new Rate(x.id, x.toCurrency.currency,x.toCurrency.rate),
                            x.fee
                        )
                    })
                    this.fees = list;
                    this.feesSubject.next(this.fees);
                }
            }
        })
        return this.feesSubject.asObservable();
    }


    public updateFee(fee:Fee){
        this.apiService.updateFee(fee).subscribe({
            next:(data)=>{
                console.log(data)
            },
            error(err) {
                console.error(err)
            },
        })
    }

    public saveFee(fee:Fee){
        this.apiService.saveFee(fee).subscribe({
            next:(response)=>{
                if(response.ok){
                    let result : IFee = response.result
                    let newFee = new Fee(
                        result.id,
                        new Rate(result.id, result.fromCurrency.currency, result.fromCurrency.rate),
                        new Rate(result.id, result.toCurrency.currency, result.toCurrency.rate),
                        result.fee
                    )
                    this.fees.push(newFee)
                    console.log("new Fee")
                    console.log(result)
                    console.log(newFee)
                    this.feesSubject.next(this.fees);
                }
            },
            error(err) {
                console.error(err)
            },
        })
    }

    public deleteFee(fee:Fee){
        this.apiService.deleteFee(fee).subscribe({
            next:(response)=>{
               console.log(response.result)
               if(response.ok == true){
                 let index = this.fees.findIndex(x=>x.id == fee.id)
                 if(index > -1){
                    this.fees.splice(index)
                 }
               }
               this.feesSubject.next(this.fees)
            },
            error(err) {
                console.log(err)
            },
        })
    }


    rates: Rate[] = []

    private ratesSubject = new BehaviorSubject<Rate[]>(this.rates);
    getRates():Observable<Rate[]> {
        this.apiService.getRates().subscribe({
            next:(response)=>{
                if(response.ok == true){
                    let list = (response.result as IRate[]).map(x=>{
                        return new Rate(x.id,x.currency, x.rate)
                    })
                    this.rates = list;
                    this.ratesSubject.next(this.rates);
                }
            }
        })
        return this.ratesSubject.asObservable();
    }

    updateRates(){
        this.apiService.updateRates().subscribe({
            next:(response)=>{
                if(response.ok){
                    let list = (response.result as IRate[]).map(x=>{
                        return new Rate(x.id,x.currency, x.rate)
                    })
                    this.rates = list;
                    this.ratesSubject.next(this.rates);
                }
            },
            error(err) {
                console.error(err)
            },
        })
    }

}
