import { IRate } from "../rates/IRate";

export interface IRatesResponse{
    
    ok:boolean
    result:IRate[]
    error?: string
    errorCode? : number

}