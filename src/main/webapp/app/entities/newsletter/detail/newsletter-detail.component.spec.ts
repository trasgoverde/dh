import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NewsletterDetailComponent } from './newsletter-detail.component';

describe('Component Tests', () => {
  describe('Newsletter Management Detail Component', () => {
    let comp: NewsletterDetailComponent;
    let fixture: ComponentFixture<NewsletterDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [NewsletterDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ newsletter: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(NewsletterDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NewsletterDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load newsletter on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.newsletter).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
