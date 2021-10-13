import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPhoto, Photo } from '../photo.model';
import { PhotoService } from '../service/photo.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IAlbum } from 'app/entities/album/album.model';
import { AlbumService } from 'app/entities/album/service/album.service';
import { ICalbum } from 'app/entities/calbum/calbum.model';
import { CalbumService } from 'app/entities/calbum/service/calbum.service';

@Component({
  selector: 'jhi-photo-update',
  templateUrl: './photo-update.component.html',
})
export class PhotoUpdateComponent implements OnInit {
  isSaving = false;

  albumsSharedCollection: IAlbum[] = [];
  calbumsSharedCollection: ICalbum[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    image: [],
    imageContentType: [],
    album: [],
    calbum: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected photoService: PhotoService,
    protected albumService: AlbumService,
    protected calbumService: CalbumService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ photo }) => {
      if (photo.id === undefined) {
        const today = dayjs().startOf('day');
        photo.creationDate = today;
      }

      this.updateForm(photo);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('dhApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const photo = this.createFromForm();
    if (photo.id !== undefined) {
      this.subscribeToSaveResponse(this.photoService.update(photo));
    } else {
      this.subscribeToSaveResponse(this.photoService.create(photo));
    }
  }

  trackAlbumById(index: number, item: IAlbum): number {
    return item.id!;
  }

  trackCalbumById(index: number, item: ICalbum): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPhoto>>): void {
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

  protected updateForm(photo: IPhoto): void {
    this.editForm.patchValue({
      id: photo.id,
      creationDate: photo.creationDate ? photo.creationDate.format(DATE_TIME_FORMAT) : null,
      image: photo.image,
      imageContentType: photo.imageContentType,
      album: photo.album,
      calbum: photo.calbum,
    });

    this.albumsSharedCollection = this.albumService.addAlbumToCollectionIfMissing(this.albumsSharedCollection, photo.album);
    this.calbumsSharedCollection = this.calbumService.addCalbumToCollectionIfMissing(this.calbumsSharedCollection, photo.calbum);
  }

  protected loadRelationshipsOptions(): void {
    this.albumService
      .query()
      .pipe(map((res: HttpResponse<IAlbum[]>) => res.body ?? []))
      .pipe(map((albums: IAlbum[]) => this.albumService.addAlbumToCollectionIfMissing(albums, this.editForm.get('album')!.value)))
      .subscribe((albums: IAlbum[]) => (this.albumsSharedCollection = albums));

    this.calbumService
      .query()
      .pipe(map((res: HttpResponse<ICalbum[]>) => res.body ?? []))
      .pipe(map((calbums: ICalbum[]) => this.calbumService.addCalbumToCollectionIfMissing(calbums, this.editForm.get('calbum')!.value)))
      .subscribe((calbums: ICalbum[]) => (this.calbumsSharedCollection = calbums));
  }

  protected createFromForm(): IPhoto {
    return {
      ...new Photo(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      album: this.editForm.get(['album'])!.value,
      calbum: this.editForm.get(['calbum'])!.value,
    };
  }
}
