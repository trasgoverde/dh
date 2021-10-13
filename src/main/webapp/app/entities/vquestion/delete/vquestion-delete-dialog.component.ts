import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVquestion } from '../vquestion.model';
import { VquestionService } from '../service/vquestion.service';

@Component({
  templateUrl: './vquestion-delete-dialog.component.html',
})
export class VquestionDeleteDialogComponent {
  vquestion?: IVquestion;

  constructor(protected vquestionService: VquestionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vquestionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
