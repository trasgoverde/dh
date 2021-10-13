jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CmessageService } from '../service/cmessage.service';
import { ICmessage, Cmessage } from '../cmessage.model';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

import { CmessageUpdateComponent } from './cmessage-update.component';

describe('Component Tests', () => {
  describe('Cmessage Management Update Component', () => {
    let comp: CmessageUpdateComponent;
    let fixture: ComponentFixture<CmessageUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let cmessageService: CmessageService;
    let communityService: CommunityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CmessageUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CmessageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CmessageUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      cmessageService = TestBed.inject(CmessageService);
      communityService = TestBed.inject(CommunityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Community query and add missing value', () => {
        const cmessage: ICmessage = { id: 456 };
        const csender: ICommunity = { id: 56847 };
        cmessage.csender = csender;
        const creceiver: ICommunity = { id: 6781 };
        cmessage.creceiver = creceiver;

        const communityCollection: ICommunity[] = [{ id: 22390 }];
        jest.spyOn(communityService, 'query').mockReturnValue(of(new HttpResponse({ body: communityCollection })));
        const additionalCommunities = [csender, creceiver];
        const expectedCollection: ICommunity[] = [...additionalCommunities, ...communityCollection];
        jest.spyOn(communityService, 'addCommunityToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cmessage });
        comp.ngOnInit();

        expect(communityService.query).toHaveBeenCalled();
        expect(communityService.addCommunityToCollectionIfMissing).toHaveBeenCalledWith(communityCollection, ...additionalCommunities);
        expect(comp.communitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const cmessage: ICmessage = { id: 456 };
        const csender: ICommunity = { id: 42737 };
        cmessage.csender = csender;
        const creceiver: ICommunity = { id: 82679 };
        cmessage.creceiver = creceiver;

        activatedRoute.data = of({ cmessage });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(cmessage));
        expect(comp.communitiesSharedCollection).toContain(csender);
        expect(comp.communitiesSharedCollection).toContain(creceiver);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Cmessage>>();
        const cmessage = { id: 123 };
        jest.spyOn(cmessageService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cmessage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cmessage }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(cmessageService.update).toHaveBeenCalledWith(cmessage);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Cmessage>>();
        const cmessage = new Cmessage();
        jest.spyOn(cmessageService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cmessage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cmessage }));
        saveSubject.complete();

        // THEN
        expect(cmessageService.create).toHaveBeenCalledWith(cmessage);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Cmessage>>();
        const cmessage = { id: 123 };
        jest.spyOn(cmessageService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cmessage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(cmessageService.update).toHaveBeenCalledWith(cmessage);
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
