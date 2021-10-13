import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NewsletterComponent } from './list/newsletter.component';
import { NewsletterDetailComponent } from './detail/newsletter-detail.component';
import { NewsletterUpdateComponent } from './update/newsletter-update.component';
import { NewsletterDeleteDialogComponent } from './delete/newsletter-delete-dialog.component';
import { NewsletterRoutingModule } from './route/newsletter-routing.module';

@NgModule({
  imports: [SharedModule, NewsletterRoutingModule],
  declarations: [NewsletterComponent, NewsletterDetailComponent, NewsletterUpdateComponent, NewsletterDeleteDialogComponent],
  entryComponents: [NewsletterDeleteDialogComponent],
})
export class NewsletterModule {}
