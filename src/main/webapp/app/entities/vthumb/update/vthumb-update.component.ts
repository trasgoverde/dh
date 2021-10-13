import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IVthumb, Vthumb } from '../vthumb.model';
import { VthumbService } from '../service/vthumb.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { IVquestion } from 'app/entities/vquestion/vquestion.model';
import { VquestionService } from 'app/entities/vquestion/service/vquestion.service';
import { IVanswer } from 'app/entities/vanswer/vanswer.model';
import { VanswerService } from 'app/entities/vanswer/service/vanswer.service';

@Component({
  selector: 'jhi-vthumb-update',
  templateUrl: './vthumb-update.component.html',
})
export class VthumbUpdateComponent implements OnInit {
  isSaving = false;

  appusersSharedCollection: IAppuser[] = [];
  vquestionsSharedCollection: IVquestion[] = [];
  vanswersSharedCollection: IVanswer[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    vthumbUp: [],
    vthumbDown: [],
    appuser: [null, Validators.required],
    vquestion: [],
    vanswer: [],
  });

  constructor(
    protected vthumbService: VthumbService,
    protected appuserService: AppuserService,
    protected vquestionService: VquestionService,
    protected vanswerService: VanswerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vthumb }) => {
      if (vthumb.id === undefined) {
        const today = dayjs().startOf('day');
        vthumb.creationDate = today;
      }

      this.updateForm(vthumb);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vthumb = this.createFromForm();
    if (vthumb.id !== undefined) {
      this.subscribeToSaveResponse(this.vthumbService.update(vthumb));
    } else {
      this.subscribeToSaveResponse(this.vthumbService.create(vthumb));
    }
  }

  trackAppuserById(index: number, item: IAppuser): number {
    return item.id!;
  }

  trackVquestionById(index: number, item: IVquestion): number {
    return item.id!;
  }

  trackVanswerById(index: number, item: IVanswer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVthumb>>): void {
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

  protected updateForm(vthumb: IVthumb): void {
    this.editForm.patchValue({
      id: vthumb.id,
      creationDate: vthumb.creationDate ? vthumb.creationDate.format(DATE_TIME_FORMAT) : null,
      vthumbUp: vthumb.vthumbUp,
      vthumbDown: vthumb.vthumbDown,
      appuser: vthumb.appuser,
      vquestion: vthumb.vquestion,
      vanswer: vthumb.vanswer,
    });

    this.appusersSharedCollection = this.appuserService.addAppuserToCollectionIfMissing(this.appusersSharedCollection, vthumb.appuser);
    this.vquestionsSharedCollection = this.vquestionService.addVquestionToCollectionIfMissing(
      this.vquestionsSharedCollection,
      vthumb.vquestion
    );
    this.vanswersSharedCollection = this.vanswerService.addVanswerToCollectionIfMissing(this.vanswersSharedCollection, vthumb.vanswer);
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

    this.vanswerService
      .query()
      .pipe(map((res: HttpResponse<IVanswer[]>) => res.body ?? []))
      .pipe(
        map((vanswers: IVanswer[]) => this.vanswerService.addVanswerToCollectionIfMissing(vanswers, this.editForm.get('vanswer')!.value))
      )
      .subscribe((vanswers: IVanswer[]) => (this.vanswersSharedCollection = vanswers));
  }

  protected createFromForm(): IVthumb {
    return {
      ...new Vthumb(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      vthumbUp: this.editForm.get(['vthumbUp'])!.value,
      vthumbDown: this.editForm.get(['vthumbDown'])!.value,
      appuser: this.editForm.get(['appuser'])!.value,
      vquestion: this.editForm.get(['vquestion'])!.value,
      vanswer: this.editForm.get(['vanswer'])!.value,
    };
  }
}
