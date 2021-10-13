jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CalbumService } from '../service/calbum.service';
import { ICalbum, Calbum } from '../calbum.model';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

import { CalbumUpdateComponent } from './calbum-update.component';

describe('Component Tests', () => {
  describe('Calbum Management Update Component', () => {
    let comp: CalbumUpdateComponent;
    let fixture: ComponentFixture<CalbumUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let calbumService: CalbumService;
    let communityService: CommunityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CalbumUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CalbumUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CalbumUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      calbumService = TestBed.inject(CalbumService);
      communityService = TestBed.inject(CommunityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Community query and add missing value', () => {
        const calbum: ICalbum = { id: 456 };
        const community: ICommunity = { id: 61264 };
        calbum.community = community;

        const communityCollection: ICommunity[] = [{ id: 35177 }];
        jest.spyOn(communityService, 'query').mockReturnValue(of(new HttpResponse({ body: communityCollection })));
        const additionalCommunities = [community];
        const expectedCollection: ICommunity[] = [...additionalCommunities, ...communityCollection];
        jest.spyOn(communityService, 'addCommunityToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ calbum });
        comp.ngOnInit();

        expect(communityService.query).toHaveBeenCalled();
        expect(communityService.addCommunityToCollectionIfMissing).toHaveBeenCalledWith(communityCollection, ...additionalCommunities);
        expect(comp.communitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const calbum: ICalbum = { id: 456 };
        const community: ICommunity = { id: 45224 };
        calbum.community = community;

        activatedRoute.data = of({ calbum });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(calbum));
        expect(comp.communitiesSharedCollection).toContain(community);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Calbum>>();
        const calbum = { id: 123 };
        jest.spyOn(calbumService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ calbum });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: calbum }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(calbumService.update).toHaveBeenCalledWith(calbum);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Calbum>>();
        const calbum = new Calbum();
        jest.spyOn(calbumService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ calbum });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: calbum }));
        saveSubject.complete();

        // THEN
        expect(calbumService.create).toHaveBeenCalledWith(calbum);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Calbum>>();
        const calbum = { id: 123 };
        jest.spyOn(calbumService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ calbum });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(calbumService.update).toHaveBeenCalledWith(calbum);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCommunityById', () => {
        it('Should return tracked Community primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCommunityById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
