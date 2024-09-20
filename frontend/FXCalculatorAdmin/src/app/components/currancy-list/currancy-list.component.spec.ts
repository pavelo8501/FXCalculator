import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrancyListComponent } from './currancy-list.component';

describe('CurrancyListComponent', () => {
  let component: CurrancyListComponent;
  let fixture: ComponentFixture<CurrancyListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CurrancyListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CurrancyListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
