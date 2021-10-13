import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VanswerDetailComponent } from './vanswer-detail.component';

describe('Component Tests', () => {
  describe('Vanswer Management Detail Component', () => {
    let comp: VanswerDetailComponent;
    let fixture: ComponentFixture<VanswerDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [VanswerDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ vanswer: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(VanswerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VanswerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load vanswer on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.vanswer).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
