import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVthumb } from '../vthumb.model';
import { VthumbService } from '../service/vthumb.service';

@Component({
  templateUrl: './vthumb-delete-dialog.component.html',
})
export class VthumbDeleteDialogComponent {
  vthumb?: IVthumb;

  constructor(protected vthumbService: VthumbService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vthumbService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
