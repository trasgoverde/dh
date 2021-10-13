import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VanswerComponent } from './list/vanswer.component';
import { VanswerDetailComponent } from './detail/vanswer-detail.component';
import { VanswerUpdateComponent } from './update/vanswer-update.component';
import { VanswerDeleteDialogComponent } from './delete/vanswer-delete-dialog.component';
import { VanswerRoutingModule } from './route/vanswer-routing.module';

@NgModule({
  imports: [SharedModule, VanswerRoutingModule],
  declarations: [VanswerComponent, VanswerDetailComponent, VanswerUpdateComponent, VanswerDeleteDialogComponent],
  entryComponents: [VanswerDeleteDialogComponent],
})
export class VanswerModule {}
