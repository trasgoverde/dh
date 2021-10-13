import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VquestionComponent } from '../list/vquestion.component';
import { VquestionDetailComponent } from '../detail/vquestion-detail.component';
import { VquestionUpdateComponent } from '../update/vquestion-update.component';
import { VquestionRoutingResolveService } from './vquestion-routing-resolve.service';

const vquestionRoute: Routes = [
  {
    path: '',
    component: VquestionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VquestionDetailComponent,
    resolve: {
      vquestion: VquestionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VquestionUpdateComponent,
    resolve: {
      vquestion: VquestionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VquestionUpdateComponent,
    resolve: {
      vquestion: VquestionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vquestionRoute)],
  exports: [RouterModule],
})
export class VquestionRoutingModule {}
