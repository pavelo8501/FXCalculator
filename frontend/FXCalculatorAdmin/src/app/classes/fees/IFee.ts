import { IRate } from "../rates/IRate"


export interface IFee{
    id:number
    currencyFrom:IRate
    currencyTo:IRate
    fee:number
}