import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VtopicComponent } from '../list/vtopic.component';
import { VtopicDetailComponent } from '../detail/vtopic-detail.component';
import { VtopicUpdateComponent } from '../update/vtopic-update.component';
import { VtopicRoutingResolveService } from './vtopic-routing-resolve.service';

const vtopicRoute: Routes = [
  {
    path: '',
    component: VtopicComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VtopicDetailComponent,
    resolve: {
      vtopic: VtopicRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VtopicUpdateComponent,
    resolve: {
      vtopic: VtopicRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VtopicUpdateComponent,
    resolve: {
      vtopic: VtopicRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vtopicRoute)],
  exports: [RouterModule],
})
export class VtopicRoutingModule {}
