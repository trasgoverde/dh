import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVanswer } from '../vanswer.model';
import { VanswerService } from '../service/vanswer.service';

@Component({
  templateUrl: './vanswer-delete-dialog.component.html',
})
export class VanswerDeleteDialogComponent {
  vanswer?: IVanswer;

  constructor(protected vanswerService: VanswerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vanswerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
