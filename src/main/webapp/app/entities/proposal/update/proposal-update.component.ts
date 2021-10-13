import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProposal, Proposal } from '../proposal.model';
import { ProposalService } from '../service/proposal.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { IPost } from 'app/entities/post/post.model';
import { PostService } from 'app/entities/post/service/post.service';

@Component({
  selector: 'jhi-proposal-update',
  templateUrl: './proposal-update.component.html',
})
export class ProposalUpdateComponent implements OnInit {
  isSaving = false;

  appusersSharedCollection: IAppuser[] = [];
  postsSharedCollection: IPost[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    proposalName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(250)]],
    proposalType: [null, [Validators.required]],
    proposalRole: [null, [Validators.required]],
    releaseDate: [],
    isOpen: [],
    isAccepted: [],
    isPaid: [],
    appuser: [],
    post: [],
  });

  constructor(
    protected proposalService: ProposalService,
    protected appuserService: AppuserService,
    protected postService: PostService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proposal }) => {
      if (proposal.id === undefined) {
        const today = dayjs().startOf('day');
        proposal.creationDate = today;
        proposal.releaseDate = today;
      }

      this.updateForm(proposal);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proposal = this.createFromForm();
    if (proposal.id !== undefined) {
      this.subscribeToSaveResponse(this.proposalService.update(proposal));
    } else {
      this.subscribeToSaveResponse(this.proposalService.create(proposal));
    }
  }

  trackAppuserById(index: number, item: IAppuser): number {
    return item.id!;
  }

  trackPostById(index: number, item: IPost): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProposal>>): void {
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

  protected updateForm(proposal: IProposal): void {
    this.editForm.patchValue({
      id: proposal.id,
      creationDate: proposal.creationDate ? proposal.creationDate.format(DATE_TIME_FORMAT) : null,
      proposalName: proposal.proposalName,
      proposalType: proposal.proposalType,
      proposalRole: proposal.proposalRole,
      releaseDate: proposal.releaseDate ? proposal.releaseDate.format(DATE_TIME_FORMAT) : null,
      isOpen: proposal.isOpen,
      isAccepted: proposal.isAccepted,
      isPaid: proposal.isPaid,
      appuser: proposal.appuser,
      post: proposal.post,
    });

    this.appusersSharedCollection = this.appuserService.addAppuserToCollectionIfMissing(this.appusersSharedCollection, proposal.appuser);
    this.postsSharedCollection = this.postService.addPostToCollectionIfMissing(this.postsSharedCollection, proposal.post);
  }

  protected loadRelationshipsOptions(): void {
    this.appuserService
      .query()
      .pipe(map((res: HttpResponse<IAppuser[]>) => res.body ?? []))
      .pipe(
        map((appusers: IAppuser[]) => this.appuserService.addAppuserToCollectionIfMissing(appusers, this.editForm.get('appuser')!.value))
      )
      .subscribe((appusers: IAppuser[]) => (this.appusersSharedCollection = appusers));

    this.postService
      .query()
      .pipe(map((res: HttpResponse<IPost[]>) => res.body ?? []))
      .pipe(map((posts: IPost[]) => this.postService.addPostToCollectionIfMissing(posts, this.editForm.get('post')!.value)))
      .subscribe((posts: IPost[]) => (this.postsSharedCollection = posts));
  }

  protected createFromForm(): IProposal {
    return {
      ...new Proposal(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      proposalName: this.editForm.get(['proposalName'])!.value,
      proposalType: this.editForm.get(['proposalType'])!.value,
      proposalRole: this.editForm.get(['proposalRole'])!.value,
      releaseDate: this.editForm.get(['releaseDate'])!.value
        ? dayjs(this.editForm.get(['releaseDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      isOpen: this.editForm.get(['isOpen'])!.value,
      isAccepted: this.editForm.get(['isAccepted'])!.value,
      isPaid: this.editForm.get(['isPaid'])!.value,
      appuser: this.editForm.get(['appuser'])!.value,
      post: this.editForm.get(['post'])!.value,
    };
  }
}
