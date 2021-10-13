import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IVanswer, Vanswer } from '../vanswer.model';
import { VanswerService } from '../service/vanswer.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { IVquestion } from 'app/entities/vquestion/vquestion.model';
import { VquestionService } from 'app/entities/vquestion/service/vquestion.service';

@Component({
  selector: 'jhi-vanswer-update',
  templateUrl: './vanswer-update.component.html',
})
export class VanswerUpdateComponent implements OnInit {
  isSaving = false;

  appusersSharedCollection: IAppuser[] = [];
  vquestionsSharedCollection: IVquestion[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    urlVanswer: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(500)]],
    accepted: [],
    appuser: [null, Validators.required],
    vquestion: [null, Validators.required],
  });

  constructor(
    protected vanswerService: VanswerService,
    protected appuserService: AppuserService,
    protected vquestionService: VquestionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vanswer }) => {
      if (vanswer.id === undefined) {
        const today = dayjs().startOf('day');
        vanswer.creationDate = today;
      }

      this.updateForm(vanswer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vanswer = this.createFromForm();
    if (vanswer.id !== undefined) {
      this.subscribeToSaveResponse(this.vanswerService.update(vanswer));
    } else {
      this.subscribeToSaveResponse(this.vanswerService.create(vanswer));
    }
  }

  trackAppuserById(index: number, item: IAppuser): number {
    return item.id!;
  }

  trackVquestionById(index: number, item: IVquestion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVanswer>>): void {
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

  protected updateForm(vanswer: IVanswer): void {
    this.editForm.patchValue({
      id: vanswer.id,
      creationDate: vanswer.creationDate ? vanswer.creationDate.format(DATE_TIME_FORMAT) : null,
      urlVanswer: vanswer.urlVanswer,
      accepted: vanswer.accepted,
      appuser: vanswer.appuser,
      vquestion: vanswer.vquestion,
    });

    this.appusersSharedCollection = this.appuserService.addAppuserToCollectionIfMissing(this.appusersSharedCollection, vanswer.appuser);
    this.vquestionsSharedCollection = this.vquestionService.addVquestionToCollectionIfMissing(
      this.vquestionsSharedCollection,
      vanswer.vquestion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appuserService
      .query()
      .pipe(map((res: HttpResponse<IAppuser[]>) => res.body ?? []))
      .pipe(
        map((appusers: IAppuser[]) => this.appuserService.addAppuserToCollectionIfMissing(appusers, this.editForm.get('appuser')!.value))
      )
      .subscribe((appusers: IAppuser[]) => (this.appusersSharedCollection = appusers));

    this.vquestionService
      .query()
      .pipe(map((res: HttpResponse<IVquestion[]>) => res.body ?? []))
      .pipe(
        map((vquestions: IVquestion[]) =>
          this.vquestionService.addVquestionToCollectionIfMissing(vquestions, this.editForm.get('vquestion')!.value)
        )
      )
      .subscribe((vquestions: IVquestion[]) => (this.vquestionsSharedCollection = vquestions));
  }

  protected createFromForm(): IVanswer {
    return {
      ...new Vanswer(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      urlVanswer: this.editForm.get(['urlVanswer'])!.value,
      accepted: this.editForm.get(['accepted'])!.value,
      appuser: this.editForm.get(['appuser'])!.value,
      vquestion: this.editForm.get(['vquestion'])!.value,
    };
  }
}
