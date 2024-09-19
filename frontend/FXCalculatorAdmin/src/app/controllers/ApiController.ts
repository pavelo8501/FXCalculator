import { BehaviorSubject, Observable } from "rxjs";
import { Fee } from "../classes/fees/Fee";
import { Rate } from "../classes/rates/Rate";
import { ApiService } from "../services/api.service";
import { Injectable } from "@angular/core";
import { IFee } from "../classes/fees/IFee";


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
                    let list = (response.data as  IFee[]).map(x=>{
                        return new Fee(
                            x.id,
                            new Rate(x.id, x.currencyFrom.currency, x.currencyFrom.rate),
                            new Rate(x.id, x.currencyTo.currency,x.currencyTo.rate),
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

}
