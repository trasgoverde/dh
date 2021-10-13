import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CmessageComponent } from './list/cmessage.component';
import { CmessageDetailComponent } from './detail/cmessage-detail.component';
import { CmessageUpdateComponent } from './update/cmessage-update.component';
import { CmessageDeleteDialogComponent } from './delete/cmessage-delete-dialog.component';
import { CmessageRoutingModule } from './route/cmessage-routing.module';

@NgModule({
  imports: [SharedModule, CmessageRoutingModule],
  declarations: [CmessageComponent, CmessageDetailComponent, CmessageUpdateComponent, CmessageDeleteDialogComponent],
  entryComponents: [CmessageDeleteDialogComponent],
})
export class CmessageModule {}
