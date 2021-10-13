import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMessage, Message } from '../message.model';
import { MessageService } from '../service/message.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';

@Component({
  selector: 'jhi-message-update',
  templateUrl: './message-update.component.html',
})
export class MessageUpdateComponent implements OnInit {
  isSaving = false;

  appusersSharedCollection: IAppuser[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    messageText: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(8000)]],
    isDelivered: [],
    sender: [],
    receiver: [],
  });

  constructor(
    protected messageService: MessageService,
    protected appuserService: AppuserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ message }) => {
      if (message.id === undefined) {
        const today = dayjs().startOf('day');
        message.creationDate = today;
      }

      this.updateForm(message);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const message = this.createFromForm();
    if (message.id !== undefined) {
      this.subscribeToSaveResponse(this.messageService.update(message));
    } else {
      this.subscribeToSaveResponse(this.messageService.create(message));
    }
  }

  trackAppuserById(index: number, item: IAppuser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMessage>>): void {
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

  protected updateForm(message: IMessage): void {
    this.editForm.patchValue({
      id: message.id,
      creationDate: message.creationDate ? message.creationDate.format(DATE_TIME_FORMAT) : null,
      messageText: message.messageText,
      isDelivered: message.isDelivered,
      sender: message.sender,
      receiver: message.receiver,
    });

    this.appusersSharedCollection = this.appuserService.addAppuserToCollectionIfMissing(
      this.appusersSharedCollection,
      message.sender,
      message.receiver
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appuserService
      .query()
      .pipe(map((res: HttpResponse<IAppuser[]>) => res.body ?? []))
      .pipe(
        map((appusers: IAppuser[]) =>
          this.appuserService.addAppuserToCollectionIfMissing(
            appusers,
            this.editForm.get('sender')!.value,
            this.editForm.get('receiver')!.value
          )
        )
      )
      .subscribe((appusers: IAppuser[]) => (this.appusersSharedCollection = appusers));
  }

  protected createFromForm(): IMessage {
    return {
      ...new Message(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      messageText: this.editForm.get(['messageText'])!.value,
      isDelivered: this.editForm.get(['isDelivered'])!.value,
      sender: this.editForm.get(['sender'])!.value,
      receiver: this.editForm.get(['receiver'])!.value,
    };
  }
}
