import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IVquestion, Vquestion } from '../vquestion.model';
import { VquestionService } from '../service/vquestion.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { IVtopic } from 'app/entities/vtopic/vtopic.model';
import { VtopicService } from 'app/entities/vtopic/service/vtopic.service';

@Component({
  selector: 'jhi-vquestion-update',
  templateUrl: './vquestion-update.component.html',
})
export class VquestionUpdateComponent implements OnInit {
  isSaving = false;

  appusersSharedCollection: IAppuser[] = [];
  vtopicsSharedCollection: IVtopic[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    vquestion: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    vquestionDescription: [null, [Validators.minLength(2), Validators.maxLength(250)]],
    appuser: [null, Validators.required],
    vtopic: [null, Validators.required],
  });

  constructor(
    protected vquestionService: VquestionService,
    protected appuserService: AppuserService,
    protected vtopicService: VtopicService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vquestion }) => {
      if (vquestion.id === undefined) {
        const today = dayjs().startOf('day');
        vquestion.creationDate = today;
      }

      this.updateForm(vquestion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vquestion = this.createFromForm();
    if (vquestion.id !== undefined) {
      this.subscribeToSaveResponse(this.vquestionService.update(vquestion));
    } else {
      this.subscribeToSaveResponse(this.vquestionService.create(vquestion));
    }
  }

  trackAppuserById(index: number, item: IAppuser): number {
    return item.id!;
  }

  trackVtopicById(index: number, item: IVtopic): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVquestion>>): void {
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

  protected updateForm(vquestion: IVquestion): void {
    this.editForm.patchValue({
      id: vquestion.id,
      creationDate: vquestion.creationDate ? vquestion.creationDate.format(DATE_TIME_FORMAT) : null,
      vquestion: vquestion.vquestion,
      vquestionDescription: vquestion.vquestionDescription,
      appuser: vquestion.appuser,
      vtopic: vquestion.vtopic,
    });

    this.appusersSharedCollection = this.appuserService.addAppuserToCollectionIfMissing(this.appusersSharedCollection, vquestion.appuser);
    this.vtopicsSharedCollection = this.vtopicService.addVtopicToCollectionIfMissing(this.vtopicsSharedCollection, vquestion.vtopic);
  }

  protected loadRelationshipsOptions(): void {
    this.appuserService
      .query()
      .pipe(map((res: HttpResponse<IAppuser[]>) => res.body ?? []))
      .pipe(
        map((appusers: IAppuser[]) => this.appuserService.addAppuserToCollectionIfMissing(appusers, this.editForm.get('appuser')!.value))
      )
      .subscribe((appusers: IAppuser[]) => (this.appusersSharedCollection = appusers));

    this.vtopicService
      .query()
      .pipe(map((res: HttpResponse<IVtopic[]>) => res.body ?? []))
      .pipe(map((vtopics: IVtopic[]) => this.vtopicService.addVtopicToCollectionIfMissing(vtopics, this.editForm.get('vtopic')!.value)))
      .subscribe((vtopics: IVtopic[]) => (this.vtopicsSharedCollection = vtopics));
  }

  protected createFromForm(): IVquestion {
    return {
      ...new Vquestion(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      vquestion: this.editForm.get(['vquestion'])!.value,
      vquestionDescription: this.editForm.get(['vquestionDescription'])!.value,
      appuser: this.editForm.get(['appuser'])!.value,
      vtopic: this.editForm.get(['vtopic'])!.value,
    };
  }
}
