import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICalbum, Calbum } from '../calbum.model';
import { CalbumService } from '../service/calbum.service';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

@Component({
  selector: 'jhi-calbum-update',
  templateUrl: './calbum-update.component.html',
})
export class CalbumUpdateComponent implements OnInit {
  isSaving = false;

  communitiesSharedCollection: ICommunity[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    title: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    community: [null, Validators.required],
  });

  constructor(
    protected calbumService: CalbumService,
    protected communityService: CommunityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ calbum }) => {
      if (calbum.id === undefined) {
        const today = dayjs().startOf('day');
        calbum.creationDate = today;
      }

      this.updateForm(calbum);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const calbum = this.createFromForm();
    if (calbum.id !== undefined) {
      this.subscribeToSaveResponse(this.calbumService.update(calbum));
    } else {
      this.subscribeToSaveResponse(this.calbumService.create(calbum));
    }
  }

  trackCommunityById(index: number, item: ICommunity): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICalbum>>): void {
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

  protected updateForm(calbum: ICalbum): void {
    this.editForm.patchValue({
      id: calbum.id,
      creationDate: calbum.creationDate ? calbum.creationDate.format(DATE_TIME_FORMAT) : null,
      title: calbum.title,
      community: calbum.community,
    });

    this.communitiesSharedCollection = this.communityService.addCommunityToCollectionIfMissing(
      this.communitiesSharedCollection,
      calbum.community
    );
  }

  protected loadRelationshipsOptions(): void {
    this.communityService
      .query()
      .pipe(map((res: HttpResponse<ICommunity[]>) => res.body ?? []))
      .pipe(
        map((communities: ICommunity[]) =>
          this.communityService.addCommunityToCollectionIfMissing(communities, this.editForm.get('community')!.value)
        )
      )
      .subscribe((communities: ICommunity[]) => (this.communitiesSharedCollection = communities));
  }

  protected createFromForm(): ICalbum {
    return {
      ...new Calbum(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      title: this.editForm.get(['title'])!.value,
      community: this.editForm.get(['community'])!.value,
    };
  }
}
