import { IRate } from "./IRate";


export class Rate implements IRate{

    constructor(public id:number, public currency: string, public rate:number){

    }

    fromSource(val:IRate){

    }

}