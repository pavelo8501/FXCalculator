import { Component, input, OnInit, output, signal } from '@angular/core';
import { Rate } from '../../classes/rates/Rate';
import { ApiService } from '../../services/api.service';
import { Fee } from '../../classes/fees/Fee';

@Component({
  selector: 'app-currancy-list',
  standalone: true,
  imports: [],
  templateUrl: './currancy-list.component.html',
  styleUrl: './currancy-list.component.css'
})
export class CurrancyListComponent implements OnInit {


  partners = input<Rate[]>([]);

  onUpdate = output<Fee>();
  onDelete = output<Fee>();


  fees = input<Fee[]>([]);

  constructor(){

  }


  ngOnInit(): void {
     
  }


  addNewPair(){

  }

  updateFee(fee : Fee){
      this.onUpdate.emit(fee)
  }

  deleteFee(fee : Fee){
      this.onDelete.emit(fee);
  }



}
