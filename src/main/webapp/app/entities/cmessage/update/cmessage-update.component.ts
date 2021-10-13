import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICmessage, Cmessage } from '../cmessage.model';
import { CmessageService } from '../service/cmessage.service';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

@Component({
  selector: 'jhi-cmessage-update',
  templateUrl: './cmessage-update.component.html',
})
export class CmessageUpdateComponent implements OnInit {
  isSaving = false;

  communitiesSharedCollection: ICommunity[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    messageText: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(8000)]],
    isDelivered: [],
    csender: [],
    creceiver: [],
  });

  constructor(
    protected cmessageService: CmessageService,
    protected communityService: CommunityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cmessage }) => {
      if (cmessage.id === undefined) {
        const today = dayjs().startOf('day');
        cmessage.creationDate = today;
      }

      this.updateForm(cmessage);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cmessage = this.createFromForm();
    if (cmessage.id !== undefined) {
      this.subscribeToSaveResponse(this.cmessageService.update(cmessage));
    } else {
      this.subscribeToSaveResponse(this.cmessageService.create(cmessage));
    }
  }

  trackCommunityById(index: number, item: ICommunity): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICmessage>>): void {
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

  protected updateForm(cmessage: ICmessage): void {
    this.editForm.patchValue({
      id: cmessage.id,
      creationDate: cmessage.creationDate ? cmessage.creationDate.format(DATE_TIME_FORMAT) : null,
      messageText: cmessage.messageText,
      isDelivered: cmessage.isDelivered,
      csender: cmessage.csender,
      creceiver: cmessage.creceiver,
    });

    this.communitiesSharedCollection = this.communityService.addCommunityToCollectionIfMissing(
      this.communitiesSharedCollection,
      cmessage.csender,
      cmessage.creceiver
    );
  }

  protected loadRelationshipsOptions(): void {
    this.communityService
      .query()
      .pipe(map((res: HttpResponse<ICommunity[]>) => res.body ?? []))
      .pipe(
        map((communities: ICommunity[]) =>
          this.communityService.addCommunityToCollectionIfMissing(
            communities,
            this.editForm.get('csender')!.value,
            this.editForm.get('creceiver')!.value
          )
        )
      )
      .subscribe((communities: ICommunity[]) => (this.communitiesSharedCollection = communities));
  }

  protected createFromForm(): ICmessage {
    return {
      ...new Cmessage(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      messageText: this.editForm.get(['messageText'])!.value,
      isDelivered: this.editForm.get(['isDelivered'])!.value,
      csender: this.editForm.get(['csender'])!.value,
      creceiver: this.editForm.get(['creceiver'])!.value,
    };
  }
}
