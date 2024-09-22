import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { CurrancyListComponent } from "../../components/currancy-list/currancy-list.component";
import { Fee } from '../../classes/fees/Fee';
import { ApiController } from '../../controllers/ApiController';
import { Rate } from '../../classes/rates/Rate';

@Component({
  selector: 'home-page',
  standalone: true,
  imports: [CurrancyListComponent],
  templateUrl: './home.page.html',
  styleUrl: './home.page.css'
})
export class HomePage implements OnInit, AfterViewInit {

  constructor(private apiController: ApiController ){

  }

  rates:Rate[] = []
  fees:Fee[] = []

  ngOnInit(): void {
    this.getRates()
    this.getFees()
  }


  ngAfterViewInit(): void {
    
  }

  updateRates(){
    this.apiController.updateRates()
  }

  getRates(){
    console.log("Requesting rates")
    this.apiController.getRates().subscribe({
      next:(data)=>{
        console.log(data)
        this.rates = data
      }
    })
  }


  getFees(){
    this.apiController.getFees().subscribe({
      next:(data)=>{
        console.log("Fees updated")
        this.fees = data
      }
    })
  }

  updateFee(fee:Fee){

    if(fee.id == 0){
      this.apiController.saveFee(fee)
      console.log("saving");
      console.log(fee);
    }else{
      console.log("updating");
      console.log(fee);
      this.apiController.updateFee(fee);
    }
  }

  deleteFee(fee:Fee){
    console.log("delete");
    console.log(fee);
    this.apiController.deleteFee(fee);
  }

}
