import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CalbumComponent } from '../list/calbum.component';
import { CalbumDetailComponent } from '../detail/calbum-detail.component';
import { CalbumUpdateComponent } from '../update/calbum-update.component';
import { CalbumRoutingResolveService } from './calbum-routing-resolve.service';

const calbumRoute: Routes = [
  {
    path: '',
    component: CalbumComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CalbumDetailComponent,
    resolve: {
      calbum: CalbumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CalbumUpdateComponent,
    resolve: {
      calbum: CalbumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CalbumUpdateComponent,
    resolve: {
      calbum: CalbumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(calbumRoute)],
  exports: [RouterModule],
})
export class CalbumRoutingModule {}
