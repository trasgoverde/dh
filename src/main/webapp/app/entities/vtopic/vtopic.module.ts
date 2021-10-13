import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VtopicComponent } from './list/vtopic.component';
import { VtopicDetailComponent } from './detail/vtopic-detail.component';
import { VtopicUpdateComponent } from './update/vtopic-update.component';
import { VtopicDeleteDialogComponent } from './delete/vtopic-delete-dialog.component';
import { VtopicRoutingModule } from './route/vtopic-routing.module';

@NgModule({
  imports: [SharedModule, VtopicRoutingModule],
  declarations: [VtopicComponent, VtopicDetailComponent, VtopicUpdateComponent, VtopicDeleteDialogComponent],
  entryComponents: [VtopicDeleteDialogComponent],
})
export class VtopicModule {}
