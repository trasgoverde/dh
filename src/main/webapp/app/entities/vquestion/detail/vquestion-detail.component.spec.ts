import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VquestionDetailComponent } from './vquestion-detail.component';

describe('Component Tests', () => {
  describe('Vquestion Management Detail Component', () => {
    let comp: VquestionDetailComponent;
    let fixture: ComponentFixture<VquestionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [VquestionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ vquestion: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(VquestionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VquestionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load vquestion on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.vquestion).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
