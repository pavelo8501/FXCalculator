import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { CurrancyListComponent } from "../../components/currancy-list/currancy-list.component";
import { Fee } from '../../classes/fees/Fee';
import { ApiController } from '../../controllers/ApiController';

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


  fees:Fee[] = []

  ngOnInit(): void {
    this.getRates()
  }


  ngAfterViewInit(): void {
    
  }

  getRates(){
    console.log("requesting fees");
    this.getFees()
  }


  getFees(){
    this.apiController.getFees().subscribe({
      next:(data)=>{
        this.fees = data
      }
    })
  }

}
