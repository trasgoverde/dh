import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VanswerComponent } from '../list/vanswer.component';
import { VanswerDetailComponent } from '../detail/vanswer-detail.component';
import { VanswerUpdateComponent } from '../update/vanswer-update.component';
import { VanswerRoutingResolveService } from './vanswer-routing-resolve.service';

const vanswerRoute: Routes = [
  {
    path: '',
    component: VanswerComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VanswerDetailComponent,
    resolve: {
      vanswer: VanswerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VanswerUpdateComponent,
    resolve: {
      vanswer: VanswerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VanswerUpdateComponent,
    resolve: {
      vanswer: VanswerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vanswerRoute)],
  exports: [RouterModule],
})
export class VanswerRoutingModule {}
