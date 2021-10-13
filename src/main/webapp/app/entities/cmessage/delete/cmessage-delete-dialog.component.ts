import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICmessage } from '../cmessage.model';
import { CmessageService } from '../service/cmessage.service';

@Component({
  templateUrl: './cmessage-delete-dialog.component.html',
})
export class CmessageDeleteDialogComponent {
  cmessage?: ICmessage;

  constructor(protected cmessageService: CmessageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cmessageService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
