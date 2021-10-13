import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AppprofileComponent } from '../list/appprofile.component';
import { AppprofileDetailComponent } from '../detail/appprofile-detail.component';
import { AppprofileUpdateComponent } from '../update/appprofile-update.component';
import { AppprofileRoutingResolveService } from './appprofile-routing-resolve.service';

const appprofileRoute: Routes = [
  {
    path: '',
    component: AppprofileComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppprofileDetailComponent,
    resolve: {
      appprofile: AppprofileRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppprofileUpdateComponent,
    resolve: {
      appprofile: AppprofileRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppprofileUpdateComponent,
    resolve: {
      appprofile: AppprofileRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(appprofileRoute)],
  exports: [RouterModule],
})
export class AppprofileRoutingModule {}
