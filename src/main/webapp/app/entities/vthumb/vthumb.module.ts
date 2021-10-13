import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VthumbComponent } from './list/vthumb.component';
import { VthumbDetailComponent } from './detail/vthumb-detail.component';
import { VthumbUpdateComponent } from './update/vthumb-update.component';
import { VthumbDeleteDialogComponent } from './delete/vthumb-delete-dialog.component';
import { VthumbRoutingModule } from './route/vthumb-routing.module';

@NgModule({
  imports: [SharedModule, VthumbRoutingModule],
  declarations: [VthumbComponent, VthumbDetailComponent, VthumbUpdateComponent, VthumbDeleteDialogComponent],
  entryComponents: [VthumbDeleteDialogComponent],
})
export class VthumbModule {}
