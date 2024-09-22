import { Fee } from "../fees/Fee";
import { IFee } from "../fees/IFee";

export interface IFeesResponse{
    
    ok:boolean
    result:IFee[]
    error?: string
    errorCode? : number

}