import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProposalVote } from '../proposal-vote.model';
import { ProposalVoteService } from '../service/proposal-vote.service';

@Component({
  templateUrl: './proposal-vote-delete-dialog.component.html',
})
export class ProposalVoteDeleteDialogComponent {
  proposalVote?: IProposalVote;

  constructor(protected proposalVoteService: ProposalVoteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.proposalVoteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
