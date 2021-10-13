import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProposalVoteComponent } from '../list/proposal-vote.component';
import { ProposalVoteDetailComponent } from '../detail/proposal-vote-detail.component';
import { ProposalVoteUpdateComponent } from '../update/proposal-vote-update.component';
import { ProposalVoteRoutingResolveService } from './proposal-vote-routing-resolve.service';

const proposalVoteRoute: Routes = [
  {
    path: '',
    component: ProposalVoteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProposalVoteDetailComponent,
    resolve: {
      proposalVote: ProposalVoteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProposalVoteUpdateComponent,
    resolve: {
      proposalVote: ProposalVoteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProposalVoteUpdateComponent,
    resolve: {
      proposalVote: ProposalVoteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(proposalVoteRoute)],
  exports: [RouterModule],
})
export class ProposalVoteRoutingModule {}
