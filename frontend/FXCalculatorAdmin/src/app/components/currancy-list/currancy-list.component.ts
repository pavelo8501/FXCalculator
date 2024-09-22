import { Component, input, effect, OnInit, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormsModule} from '@angular/forms';
import { Rate } from '../../classes/rates/Rate';
import { Fee } from '../../classes/fees/Fee';
import {MatSelectModule} from '@angular/material/select';
import { MatSelect } from '@angular/material/select';

@Component({
  selector: 'app-currancy-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatSelectModule,
    MatSelect
  ],
  templateUrl: './currancy-list.component.html',
  styleUrl: './currancy-list.component.css'
})
export class CurrancyListComponent implements OnInit {


  onUpdate = output<Fee>();
  onDelete = output<Fee>();


  fees = input<Fee[]>([]);

  rates = input<Rate[]>([]);

  newFee? : Fee

  constructor(){
    effect(() => {
      
      console.log(this.rates().length)
      let startCurrencyFrom = this.rates()[0]
      console.log(startCurrencyFrom)
      let startCurrencyTo = this.rates()[1]
      console.log(startCurrencyTo)
      this.newFee =  new Fee(0,startCurrencyFrom,startCurrencyTo, 0.02)
      console.log(this.newFee)

  });
  }


  ngOnInit(): void {
      
  }

  saveFee(feeToSave : Fee){
    this.onUpdate.emit(feeToSave)
  }

  updateFee(fee : Fee){
      this.onUpdate.emit(fee)
  }

  deleteFee(fee : Fee){
      this.onDelete.emit(fee);
  }



}
