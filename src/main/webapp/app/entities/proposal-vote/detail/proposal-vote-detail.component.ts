import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProposalVote } from '../proposal-vote.model';

@Component({
  selector: 'jhi-proposal-vote-detail',
  templateUrl: './proposal-vote-detail.component.html',
})
export class ProposalVoteDetailComponent implements OnInit {
  proposalVote: IProposalVote | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proposalVote }) => {
      this.proposalVote = proposalVote;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
