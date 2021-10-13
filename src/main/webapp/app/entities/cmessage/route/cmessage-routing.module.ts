import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CmessageComponent } from '../list/cmessage.component';
import { CmessageDetailComponent } from '../detail/cmessage-detail.component';
import { CmessageUpdateComponent } from '../update/cmessage-update.component';
import { CmessageRoutingResolveService } from './cmessage-routing-resolve.service';

const cmessageRoute: Routes = [
  {
    path: '',
    component: CmessageComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CmessageDetailComponent,
    resolve: {
      cmessage: CmessageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CmessageUpdateComponent,
    resolve: {
      cmessage: CmessageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CmessageUpdateComponent,
    resolve: {
      cmessage: CmessageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cmessageRoute)],
  exports: [RouterModule],
})
export class CmessageRoutingModule {}
