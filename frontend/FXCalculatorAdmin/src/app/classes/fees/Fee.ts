import { IRate } from "../rates/IRate"
import { Rate } from "../rates/Rate"
import {IFee } from "./IFee"


export class Fee implements IFee{
 

    constructor(public id:number, public fromCurrency: Rate, public toCurrency:Rate, public fee:number){

    }

}
