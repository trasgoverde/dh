import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProposal } from '../proposal.model';

@Component({
  selector: 'jhi-proposal-detail',
  templateUrl: './proposal-detail.component.html',
})
export class ProposalDetailComponent implements OnInit {
  proposal: IProposal | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proposal }) => {
      this.proposal = proposal;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
