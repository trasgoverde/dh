import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppprofileDetailComponent } from './appprofile-detail.component';

describe('Component Tests', () => {
  describe('Appprofile Management Detail Component', () => {
    let comp: AppprofileDetailComponent;
    let fixture: ComponentFixture<AppprofileDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AppprofileDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ appprofile: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AppprofileDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AppprofileDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load appprofile on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.appprofile).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
