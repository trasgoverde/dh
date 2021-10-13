import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICalbum } from '../calbum.model';
import { CalbumService } from '../service/calbum.service';

@Component({
  templateUrl: './calbum-delete-dialog.component.html',
})
export class CalbumDeleteDialogComponent {
  calbum?: ICalbum;

  constructor(protected calbumService: CalbumService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.calbumService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
