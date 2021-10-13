import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAppprofile, Appprofile } from '../appprofile.model';
import { AppprofileService } from '../service/appprofile.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';

@Component({
  selector: 'jhi-appprofile-update',
  templateUrl: './appprofile-update.component.html',
})
export class AppprofileUpdateComponent implements OnInit {
  isSaving = false;

  appusersCollection: IAppuser[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    gender: [],
    phone: [null, [Validators.maxLength(20)]],
    bio: [null, [Validators.maxLength(7500)]],
    facebook: [null, [Validators.maxLength(50)]],
    twitter: [null, [Validators.maxLength(50)]],
    linkedin: [null, [Validators.maxLength(50)]],
    instagram: [null, [Validators.maxLength(50)]],
    googlePlus: [null, [Validators.maxLength(50)]],
    birthdate: [],
    civilStatus: [],
    lookingFor: [],
    purpose: [],
    physical: [],
    religion: [],
    ethnicGroup: [],
    studies: [],
    sibblings: [null, [Validators.min(-1), Validators.max(20)]],
    eyes: [],
    smoker: [],
    children: [],
    futureChildren: [],
    pet: [],
    appuser: [null, Validators.required],
  });

  constructor(
    protected appprofileService: AppprofileService,
    protected appuserService: AppuserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appprofile }) => {
      if (appprofile.id === undefined) {
        const today = dayjs().startOf('day');
        appprofile.creationDate = today;
        appprofile.birthdate = today;
      }

      this.updateForm(appprofile);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appprofile = this.createFromForm();
    if (appprofile.id !== undefined) {
      this.subscribeToSaveResponse(this.appprofileService.update(appprofile));
    } else {
      this.subscribeToSaveResponse(this.appprofileService.create(appprofile));
    }
  }

  trackAppuserById(index: number, item: IAppuser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppprofile>>): void {
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

  protected updateForm(appprofile: IAppprofile): void {
    this.editForm.patchValue({
      id: appprofile.id,
      creationDate: appprofile.creationDate ? appprofile.creationDate.format(DATE_TIME_FORMAT) : null,
      gender: appprofile.gender,
      phone: appprofile.phone,
      bio: appprofile.bio,
      facebook: appprofile.facebook,
      twitter: appprofile.twitter,
      linkedin: appprofile.linkedin,
      instagram: appprofile.instagram,
      googlePlus: appprofile.googlePlus,
      birthdate: appprofile.birthdate ? appprofile.birthdate.format(DATE_TIME_FORMAT) : null,
      civilStatus: appprofile.civilStatus,
      lookingFor: appprofile.lookingFor,
      purpose: appprofile.purpose,
      physical: appprofile.physical,
      religion: appprofile.religion,
      ethnicGroup: appprofile.ethnicGroup,
      studies: appprofile.studies,
      sibblings: appprofile.sibblings,
      eyes: appprofile.eyes,
      smoker: appprofile.smoker,
      children: appprofile.children,
      futureChildren: appprofile.futureChildren,
      pet: appprofile.pet,
      appuser: appprofile.appuser,
    });

    this.appusersCollection = this.appuserService.addAppuserToCollectionIfMissing(this.appusersCollection, appprofile.appuser);
  }

  protected loadRelationshipsOptions(): void {
    this.appuserService
      .query({ 'appprofileId.specified': 'false' })
      .pipe(map((res: HttpResponse<IAppuser[]>) => res.body ?? []))
      .pipe(
        map((appusers: IAppuser[]) => this.appuserService.addAppuserToCollectionIfMissing(appusers, this.editForm.get('appuser')!.value))
      )
      .subscribe((appusers: IAppuser[]) => (this.appusersCollection = appusers));
  }

  protected createFromForm(): IAppprofile {
    return {
      ...new Appprofile(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      gender: this.editForm.get(['gender'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      bio: this.editForm.get(['bio'])!.value,
      facebook: this.editForm.get(['facebook'])!.value,
      twitter: this.editForm.get(['twitter'])!.value,
      linkedin: this.editForm.get(['linkedin'])!.value,
      instagram: this.editForm.get(['instagram'])!.value,
      googlePlus: this.editForm.get(['googlePlus'])!.value,
      birthdate: this.editForm.get(['birthdate'])!.value ? dayjs(this.editForm.get(['birthdate'])!.value, DATE_TIME_FORMAT) : undefined,
      civilStatus: this.editForm.get(['civilStatus'])!.value,
      lookingFor: this.editForm.get(['lookingFor'])!.value,
      purpose: this.editForm.get(['purpose'])!.value,
      physical: this.editForm.get(['physical'])!.value,
      religion: this.editForm.get(['religion'])!.value,
      ethnicGroup: this.editForm.get(['ethnicGroup'])!.value,
      studies: this.editForm.get(['studies'])!.value,
      sibblings: this.editForm.get(['sibblings'])!.value,
      eyes: this.editForm.get(['eyes'])!.value,
      smoker: this.editForm.get(['smoker'])!.value,
      children: this.editForm.get(['children'])!.value,
      futureChildren: this.editForm.get(['futureChildren'])!.value,
      pet: this.editForm.get(['pet'])!.value,
      appuser: this.editForm.get(['appuser'])!.value,
    };
  }
}
