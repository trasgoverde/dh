import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVtopic } from '../vtopic.model';
import { VtopicService } from '../service/vtopic.service';

@Component({
  templateUrl: './vtopic-delete-dialog.component.html',
})
export class VtopicDeleteDialogComponent {
  vtopic?: IVtopic;

  constructor(protected vtopicService: VtopicService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vtopicService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
