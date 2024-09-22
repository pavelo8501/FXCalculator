import { IRate } from "../rates/IRate"


export interface IFee{
    id:number
    fromCurrency:IRate
    toCurrency:IRate
    fee:number
}