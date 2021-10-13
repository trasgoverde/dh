import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProposalVoteComponent } from './list/proposal-vote.component';
import { ProposalVoteDetailComponent } from './detail/proposal-vote-detail.component';
import { ProposalVoteUpdateComponent } from './update/proposal-vote-update.component';
import { ProposalVoteDeleteDialogComponent } from './delete/proposal-vote-delete-dialog.component';
import { ProposalVoteRoutingModule } from './route/proposal-vote-routing.module';

@NgModule({
  imports: [SharedModule, ProposalVoteRoutingModule],
  declarations: [ProposalVoteComponent, ProposalVoteDetailComponent, ProposalVoteUpdateComponent, ProposalVoteDeleteDialogComponent],
  entryComponents: [ProposalVoteDeleteDialogComponent],
})
export class ProposalVoteModule {}
