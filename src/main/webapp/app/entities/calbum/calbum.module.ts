import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CalbumComponent } from './list/calbum.component';
import { CalbumDetailComponent } from './detail/calbum-detail.component';
import { CalbumUpdateComponent } from './update/calbum-update.component';
import { CalbumDeleteDialogComponent } from './delete/calbum-delete-dialog.component';
import { CalbumRoutingModule } from './route/calbum-routing.module';

@NgModule({
  imports: [SharedModule, CalbumRoutingModule],
  declarations: [CalbumComponent, CalbumDetailComponent, CalbumUpdateComponent, CalbumDeleteDialogComponent],
  entryComponents: [CalbumDeleteDialogComponent],
})
export class CalbumModule {}
