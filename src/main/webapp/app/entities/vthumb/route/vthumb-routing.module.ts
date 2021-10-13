import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VthumbComponent } from '../list/vthumb.component';
import { VthumbDetailComponent } from '../detail/vthumb-detail.component';
import { VthumbUpdateComponent } from '../update/vthumb-update.component';
import { VthumbRoutingResolveService } from './vthumb-routing-resolve.service';

const vthumbRoute: Routes = [
  {
    path: '',
    component: VthumbComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VthumbDetailComponent,
    resolve: {
      vthumb: VthumbRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VthumbUpdateComponent,
    resolve: {
      vthumb: VthumbRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VthumbUpdateComponent,
    resolve: {
      vthumb: VthumbRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vthumbRoute)],
  exports: [RouterModule],
})
export class VthumbRoutingModule {}
