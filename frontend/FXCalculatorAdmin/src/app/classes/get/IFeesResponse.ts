import { Fee } from "../fees/Fee";
import { IFee } from "../fees/IFee";

export interface IFeesResponse{
    
    ok:Boolean
    data:IFee[]

}