import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VtopicDetailComponent } from './vtopic-detail.component';

describe('Component Tests', () => {
  describe('Vtopic Management Detail Component', () => {
    let comp: VtopicDetailComponent;
    let fixture: ComponentFixture<VtopicDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [VtopicDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ vtopic: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(VtopicDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VtopicDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load vtopic on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.vtopic).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
