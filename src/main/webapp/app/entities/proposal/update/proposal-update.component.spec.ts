jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProposalService } from '../service/proposal.service';
import { IProposal, Proposal } from '../proposal.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { IPost } from 'app/entities/post/post.model';
import { PostService } from 'app/entities/post/service/post.service';

import { ProposalUpdateComponent } from './proposal-update.component';

describe('Component Tests', () => {
  describe('Proposal Management Update Component', () => {
    let comp: ProposalUpdateComponent;
    let fixture: ComponentFixture<ProposalUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let proposalService: ProposalService;
    let appuserService: AppuserService;
    let postService: PostService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProposalUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProposalUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProposalUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      proposalService = TestBed.inject(ProposalService);
      appuserService = TestBed.inject(AppuserService);
      postService = TestBed.inject(PostService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Appuser query and add missing value', () => {
        const proposal: IProposal = { id: 456 };
        const appuser: IAppuser = { id: 81763 };
        proposal.appuser = appuser;

        const appuserCollection: IAppuser[] = [{ id: 9093 }];
        jest.spyOn(appuserService, 'query').mockReturnValue(of(new HttpResponse({ body: appuserCollection })));
        const additionalAppusers = [appuser];
        const expectedCollection: IAppuser[] = [...additionalAppusers, ...appuserCollection];
        jest.spyOn(appuserService, 'addAppuserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ proposal });
        comp.ngOnInit();

        expect(appuserService.query).toHaveBeenCalled();
        expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(appuserCollection, ...additionalAppusers);
        expect(comp.appusersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Post query and add missing value', () => {
        const proposal: IProposal = { id: 456 };
        const post: IPost = { id: 35716 };
        proposal.post = post;

        const postCollection: IPost[] = [{ id: 54526 }];
        jest.spyOn(postService, 'query').mockReturnValue(of(new HttpResponse({ body: postCollection })));
        const additionalPosts = [post];
        const expectedCollection: IPost[] = [...additionalPosts, ...postCollection];
        jest.spyOn(postService, 'addPostToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ proposal });
        comp.ngOnInit();

        expect(postService.query).toHaveBeenCalled();
        expect(postService.addPostToCollectionIfMissing).toHaveBeenCalledWith(postCollection, ...additionalPosts);
        expect(comp.postsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const proposal: IProposal = { id: 456 };
        const appuser: IAppuser = { id: 150 };
        proposal.appuser = appuser;
        const post: IPost = { id: 30190 };
        proposal.post = post;

        activatedRoute.data = of({ proposal });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(proposal));
        expect(comp.appusersSharedCollection).toContain(appuser);
        expect(comp.postsSharedCollection).toContain(post);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Proposal>>();
        const proposal = { id: 123 };
        jest.spyOn(proposalService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ proposal });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: proposal }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(proposalService.update).toHaveBeenCalledWith(proposal);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Proposal>>();
        const proposal = new Proposal();
        jest.spyOn(proposalService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ proposal });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: proposal }));
        saveSubject.complete();

        // THEN
        expect(proposalService.create).toHaveBeenCalledWith(proposal);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Proposal>>();
        const proposal = { id: 123 };
        jest.spyOn(proposalService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ proposal });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(proposalService.update).toHaveBeenCalledWith(proposal);
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

      describe('trackPostById', () => {
        it('Should return tracked Post primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPostById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
