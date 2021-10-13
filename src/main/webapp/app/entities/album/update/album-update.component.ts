import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAlbum, Album } from '../album.model';
import { AlbumService } from '../service/album.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';

@Component({
  selector: 'jhi-album-update',
  templateUrl: './album-update.component.html',
})
export class AlbumUpdateComponent implements OnInit {
  isSaving = false;

  appusersSharedCollection: IAppuser[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    title: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    appuser: [null, Validators.required],
  });

  constructor(
    protected albumService: AlbumService,
    protected appuserService: AppuserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ album }) => {
      if (album.id === undefined) {
        const today = dayjs().startOf('day');
        album.creationDate = today;
      }

      this.updateForm(album);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const album = this.createFromForm();
    if (album.id !== undefined) {
      this.subscribeToSaveResponse(this.albumService.update(album));
    } else {
      this.subscribeToSaveResponse(this.albumService.create(album));
    }
  }

  trackAppuserById(index: number, item: IAppuser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlbum>>): void {
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

  protected updateForm(album: IAlbum): void {
    this.editForm.patchValue({
      id: album.id,
      creationDate: album.creationDate ? album.creationDate.format(DATE_TIME_FORMAT) : null,
      title: album.title,
      appuser: album.appuser,
    });

    this.appusersSharedCollection = this.appuserService.addAppuserToCollectionIfMissing(this.appusersSharedCollection, album.appuser);
  }

  protected loadRelationshipsOptions(): void {
    this.appuserService
      .query()
      .pipe(map((res: HttpResponse<IAppuser[]>) => res.body ?? []))
      .pipe(
        map((appusers: IAppuser[]) => this.appuserService.addAppuserToCollectionIfMissing(appusers, this.editForm.get('appuser')!.value))
      )
      .subscribe((appusers: IAppuser[]) => (this.appusersSharedCollection = appusers));
  }

  protected createFromForm(): IAlbum {
    return {
      ...new Album(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      title: this.editForm.get(['title'])!.value,
      appuser: this.editForm.get(['appuser'])!.value,
    };
  }
}
