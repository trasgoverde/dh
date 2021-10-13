import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProposalDetailComponent } from './proposal-detail.component';

describe('Component Tests', () => {
  describe('Proposal Management Detail Component', () => {
    let comp: ProposalDetailComponent;
    let fixture: ComponentFixture<ProposalDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ProposalDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ proposal: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ProposalDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProposalDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load proposal on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.proposal).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
