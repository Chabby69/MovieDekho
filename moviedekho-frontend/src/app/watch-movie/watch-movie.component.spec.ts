import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WatchMovieComponent } from './watch-movie.component';

describe('WatchMovieComponent', () => {
  let component: WatchMovieComponent;
  let fixture: ComponentFixture<WatchMovieComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WatchMovieComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WatchMovieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
