import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CmessageDetailComponent } from './cmessage-detail.component';

describe('Component Tests', () => {
  describe('Cmessage Management Detail Component', () => {
    let comp: CmessageDetailComponent;
    let fixture: ComponentFixture<CmessageDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CmessageDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ cmessage: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CmessageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CmessageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cmessage on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cmessage).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
