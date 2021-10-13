import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { INewsletter, Newsletter } from '../newsletter.model';
import { NewsletterService } from '../service/newsletter.service';

@Component({
  selector: 'jhi-newsletter-update',
  templateUrl: './newsletter-update.component.html',
})
export class NewsletterUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    email: [null, [Validators.required]],
  });

  constructor(protected newsletterService: NewsletterService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ newsletter }) => {
      if (newsletter.id === undefined) {
        const today = dayjs().startOf('day');
        newsletter.creationDate = today;
      }

      this.updateForm(newsletter);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const newsletter = this.createFromForm();
    if (newsletter.id !== undefined) {
      this.subscribeToSaveResponse(this.newsletterService.update(newsletter));
    } else {
      this.subscribeToSaveResponse(this.newsletterService.create(newsletter));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INewsletter>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(newsletter: INewsletter): void {
    this.editForm.patchValue({
      id: newsletter.id,
      creationDate: newsletter.creationDate ? newsletter.creationDate.format(DATE_TIME_FORMAT) : null,
      email: newsletter.email,
    });
  }

  protected createFromForm(): INewsletter {
    return {
      ...new Newsletter(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      email: this.editForm.get(['email'])!.value,
    };
  }
}
