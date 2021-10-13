import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppprofile } from '../appprofile.model';
import { AppprofileService } from '../service/appprofile.service';

@Component({
  templateUrl: './appprofile-delete-dialog.component.html',
})
export class AppprofileDeleteDialogComponent {
  appprofile?: IAppprofile;

  constructor(protected appprofileService: AppprofileService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appprofileService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
