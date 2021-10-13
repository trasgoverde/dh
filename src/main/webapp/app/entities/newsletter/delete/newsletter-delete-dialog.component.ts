import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INewsletter } from '../newsletter.model';
import { NewsletterService } from '../service/newsletter.service';

@Component({
  templateUrl: './newsletter-delete-dialog.component.html',
})
export class NewsletterDeleteDialogComponent {
  newsletter?: INewsletter;

  constructor(protected newsletterService: NewsletterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.newsletterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
