import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {ThemePalette} from '@angular/material/core';

import { HomePage } from './pages/home/home.page';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HomePage],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'FXCalculatorAdmin';



}
