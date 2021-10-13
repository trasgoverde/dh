jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProposalVoteService } from '../service/proposal-vote.service';
import { IProposalVote, ProposalVote } from '../proposal-vote.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { IProposal } from 'app/entities/proposal/proposal.model';
import { ProposalService } from 'app/entities/proposal/service/proposal.service';

import { ProposalVoteUpdateComponent } from './proposal-vote-update.component';

describe('Component Tests', () => {
  describe('ProposalVote Management Update Component', () => {
    let comp: ProposalVoteUpdateComponent;
    let fixture: ComponentFixture<ProposalVoteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let proposalVoteService: ProposalVoteService;
    let appuserService: AppuserService;
    let proposalService: ProposalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProposalVoteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProposalVoteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProposalVoteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      proposalVoteService = TestBed.inject(ProposalVoteService);
      appuserService = TestBed.inject(AppuserService);
      proposalService = TestBed.inject(ProposalService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Appuser query and add missing value', () => {
        const proposalVote: IProposalVote = { id: 456 };
        const appuser: IAppuser = { id: 2218 };
        proposalVote.appuser = appuser;

        const appuserCollection: IAppuser[] = [{ id: 69649 }];
        jest.spyOn(appuserService, 'query').mockReturnValue(of(new HttpResponse({ body: appuserCollection })));
        const additionalAppusers = [appuser];
        const expectedCollection: IAppuser[] = [...additionalAppusers, ...appuserCollection];
        jest.spyOn(appuserService, 'addAppuserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ proposalVote });
        comp.ngOnInit();

        expect(appuserService.query).toHaveBeenCalled();
        expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(appuserCollection, ...additionalAppusers);
        expect(comp.appusersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Proposal query and add missing value', () => {
        const proposalVote: IProposalVote = { id: 456 };
        const proposal: IProposal = { id: 54330 };
        proposalVote.proposal = proposal;

        const proposalCollection: IProposal[] = [{ id: 56435 }];
        jest.spyOn(proposalService, 'query').mockReturnValue(of(new HttpResponse({ body: proposalCollection })));
        const additionalProposals = [proposal];
        const expectedCollection: IProposal[] = [...additionalProposals, ...proposalCollection];
        jest.spyOn(proposalService, 'addProposalToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ proposalVote });
        comp.ngOnInit();

        expect(proposalService.query).toHaveBeenCalled();
        expect(proposalService.addProposalToCollectionIfMissing).toHaveBeenCalledWith(proposalCollection, ...additionalProposals);
        expect(comp.proposalsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const proposalVote: IProposalVote = { id: 456 };
        const appuser: IAppuser = { id: 36795 };
        proposalVote.appuser = appuser;
        const proposal: IProposal = { id: 43548 };
        proposalVote.proposal = proposal;

        activatedRoute.data = of({ proposalVote });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(proposalVote));
        expect(comp.appusersSharedCollection).toContain(appuser);
        expect(comp.proposalsSharedCollection).toContain(proposal);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ProposalVote>>();
        const proposalVote = { id: 123 };
        jest.spyOn(proposalVoteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ proposalVote });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: proposalVote }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(proposalVoteService.update).toHaveBeenCalledWith(proposalVote);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ProposalVote>>();
        const proposalVote = new ProposalVote();
        jest.spyOn(proposalVoteService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ proposalVote });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: proposalVote }));
        saveSubject.complete();

        // THEN
        expect(proposalVoteService.create).toHaveBeenCalledWith(proposalVote);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ProposalVote>>();
        const proposalVote = { id: 123 };
        jest.spyOn(proposalVoteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ proposalVote });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(proposalVoteService.update).toHaveBeenCalledWith(proposalVote);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAppuserById', () => {
        it('Should return tracked Appuser primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAppuserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackProposalById', () => {
        it('Should return tracked Proposal primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProposalById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
