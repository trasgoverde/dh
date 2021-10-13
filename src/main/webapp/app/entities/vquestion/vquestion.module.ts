import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VquestionComponent } from './list/vquestion.component';
import { VquestionDetailComponent } from './detail/vquestion-detail.component';
import { VquestionUpdateComponent } from './update/vquestion-update.component';
import { VquestionDeleteDialogComponent } from './delete/vquestion-delete-dialog.component';
import { VquestionRoutingModule } from './route/vquestion-routing.module';

@NgModule({
  imports: [SharedModule, VquestionRoutingModule],
  declarations: [VquestionComponent, VquestionDetailComponent, VquestionUpdateComponent, VquestionDeleteDialogComponent],
  entryComponents: [VquestionDeleteDialogComponent],
})
export class VquestionModule {}
