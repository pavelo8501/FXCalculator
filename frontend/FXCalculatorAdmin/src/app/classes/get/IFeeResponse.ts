import { IFee } from "../fees/IFee"



export interface IFeeResponse{
    
    ok:boolean
    result:IFee
    error?: string
    errorCode? : number

}