import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NewsletterComponent } from '../list/newsletter.component';
import { NewsletterDetailComponent } from '../detail/newsletter-detail.component';
import { NewsletterUpdateComponent } from '../update/newsletter-update.component';
import { NewsletterRoutingResolveService } from './newsletter-routing-resolve.service';

const newsletterRoute: Routes = [
  {
    path: '',
    component: NewsletterComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NewsletterDetailComponent,
    resolve: {
      newsletter: NewsletterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NewsletterUpdateComponent,
    resolve: {
      newsletter: NewsletterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NewsletterUpdateComponent,
    resolve: {
      newsletter: NewsletterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(newsletterRoute)],
  exports: [RouterModule],
})
export class NewsletterRoutingModule {}
