import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VthumbDetailComponent } from './vthumb-detail.component';

describe('Component Tests', () => {
  describe('Vthumb Management Detail Component', () => {
    let comp: VthumbDetailComponent;
    let fixture: ComponentFixture<VthumbDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [VthumbDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ vthumb: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(VthumbDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VthumbDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load vthumb on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.vthumb).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
