import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PoiMapComponent } from './poi-map.component';

describe('PoiMapComponent', () => {
  let component: PoiMapComponent;
  let fixture: ComponentFixture<PoiMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PoiMapComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PoiMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
