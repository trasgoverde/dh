import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProposalVote, ProposalVote } from '../proposal-vote.model';
import { ProposalVoteService } from '../service/proposal-vote.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { IProposal } from 'app/entities/proposal/proposal.model';
import { ProposalService } from 'app/entities/proposal/service/proposal.service';

@Component({
  selector: 'jhi-proposal-vote-update',
  templateUrl: './proposal-vote-update.component.html',
})
export class ProposalVoteUpdateComponent implements OnInit {
  isSaving = false;

  appusersSharedCollection: IAppuser[] = [];
  proposalsSharedCollection: IProposal[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    votePoints: [null, [Validators.required]],
    appuser: [],
    proposal: [],
  });

  constructor(
    protected proposalVoteService: ProposalVoteService,
    protected appuserService: AppuserService,
    protected proposalService: ProposalService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proposalVote }) => {
      if (proposalVote.id === undefined) {
        const today = dayjs().startOf('day');
        proposalVote.creationDate = today;
      }

      this.updateForm(proposalVote);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proposalVote = this.createFromForm();
    if (proposalVote.id !== undefined) {
      this.subscribeToSaveResponse(this.proposalVoteService.update(proposalVote));
    } else {
      this.subscribeToSaveResponse(this.proposalVoteService.create(proposalVote));
    }
  }

  trackAppuserById(index: number, item: IAppuser): number {
    return item.id!;
  }

  trackProposalById(index: number, item: IProposal): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProposalVote>>): void {
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

  protected updateForm(proposalVote: IProposalVote): void {
    this.editForm.patchValue({
      id: proposalVote.id,
      creationDate: proposalVote.creationDate ? proposalVote.creationDate.format(DATE_TIME_FORMAT) : null,
      votePoints: proposalVote.votePoints,
      appuser: proposalVote.appuser,
      proposal: proposalVote.proposal,
    });

    this.appusersSharedCollection = this.appuserService.addAppuserToCollectionIfMissing(
      this.appusersSharedCollection,
      proposalVote.appuser
    );
    this.proposalsSharedCollection = this.proposalService.addProposalToCollectionIfMissing(
      this.proposalsSharedCollection,
      proposalVote.proposal
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

    this.proposalService
      .query()
      .pipe(map((res: HttpResponse<IProposal[]>) => res.body ?? []))
      .pipe(
        map((proposals: IProposal[]) =>
          this.proposalService.addProposalToCollectionIfMissing(proposals, this.editForm.get('proposal')!.value)
        )
      )
      .subscribe((proposals: IProposal[]) => (this.proposalsSharedCollection = proposals));
  }

  protected createFromForm(): IProposalVote {
    return {
      ...new ProposalVote(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      votePoints: this.editForm.get(['votePoints'])!.value,
      appuser: this.editForm.get(['appuser'])!.value,
      proposal: this.editForm.get(['proposal'])!.value,
    };
  }
}
