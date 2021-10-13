import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProposalVoteDetailComponent } from './proposal-vote-detail.component';

describe('Component Tests', () => {
  describe('ProposalVote Management Detail Component', () => {
    let comp: ProposalVoteDetailComponent;
    let fixture: ComponentFixture<ProposalVoteDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ProposalVoteDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ proposalVote: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ProposalVoteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProposalVoteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load proposalVote on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.proposalVote).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
