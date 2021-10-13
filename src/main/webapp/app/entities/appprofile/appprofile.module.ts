import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AppprofileComponent } from './list/appprofile.component';
import { AppprofileDetailComponent } from './detail/appprofile-detail.component';
import { AppprofileUpdateComponent } from './update/appprofile-update.component';
import { AppprofileDeleteDialogComponent } from './delete/appprofile-delete-dialog.component';
import { AppprofileRoutingModule } from './route/appprofile-routing.module';

@NgModule({
  imports: [SharedModule, AppprofileRoutingModule],
  declarations: [AppprofileComponent, AppprofileDetailComponent, AppprofileUpdateComponent, AppprofileDeleteDialogComponent],
  entryComponents: [AppprofileDeleteDialogComponent],
})
export class AppprofileModule {}
