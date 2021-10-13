import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CalbumDetailComponent } from './calbum-detail.component';

describe('Component Tests', () => {
  describe('Calbum Management Detail Component', () => {
    let comp: CalbumDetailComponent;
    let fixture: ComponentFixture<CalbumDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CalbumDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ calbum: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CalbumDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CalbumDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load calbum on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.calbum).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
