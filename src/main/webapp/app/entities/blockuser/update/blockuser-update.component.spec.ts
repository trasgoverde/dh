jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BlockuserService } from '../service/blockuser.service';
import { IBlockuser, Blockuser } from '../blockuser.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

import { BlockuserUpdateComponent } from './blockuser-update.component';

describe('Component Tests', () => {
  describe('Blockuser Management Update Component', () => {
    let comp: BlockuserUpdateComponent;
    let fixture: ComponentFixture<BlockuserUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let blockuserService: BlockuserService;
    let appuserService: AppuserService;
    let communityService: CommunityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BlockuserUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BlockuserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BlockuserUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      blockuserService = TestBed.inject(BlockuserService);
      appuserService = TestBed.inject(AppuserService);
      communityService = TestBed.inject(CommunityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Appuser query and add missing value', () => {
        const blockuser: IBlockuser = { id: 456 };
        const blockeduser: IAppuser = { id: 5213 };
        blockuser.blockeduser = blockeduser;
        const blockinguser: IAppuser = { id: 59722 };
        blockuser.blockinguser = blockinguser;

        const appuserCollection: IAppuser[] = [{ id: 75306 }];
        jest.spyOn(appuserService, 'query').mockReturnValue(of(new HttpResponse({ body: appuserCollection })));
        const additionalAppusers = [blockeduser, blockinguser];
        const expectedCollection: IAppuser[] = [...additionalAppusers, ...appuserCollection];
        jest.spyOn(appuserService, 'addAppuserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ blockuser });
        comp.ngOnInit();

        expect(appuserService.query).toHaveBeenCalled();
        expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(appuserCollection, ...additionalAppusers);
        expect(comp.appusersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Community query and add missing value', () => {
        const blockuser: IBlockuser = { id: 456 };
        const cblockeduser: ICommunity = { id: 28886 };
        blockuser.cblockeduser = cblockeduser;
        const cblockinguser: ICommunity = { id: 41666 };
        blockuser.cblockinguser = cblockinguser;

        const communityCollection: ICommunity[] = [{ id: 64592 }];
        jest.spyOn(communityService, 'query').mockReturnValue(of(new HttpResponse({ body: communityCollection })));
        const additionalCommunities = [cblockeduser, cblockinguser];
        const expectedCollection: ICommunity[] = [...additionalCommunities, ...communityCollection];
        jest.spyOn(communityService, 'addCommunityToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ blockuser });
        comp.ngOnInit();

        expect(communityService.query).toHaveBeenCalled();
        expect(communityService.addCommunityToCollectionIfMissing).toHaveBeenCalledWith(communityCollection, ...additionalCommunities);
        expect(comp.communitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const blockuser: IBlockuser = { id: 456 };
        const blockeduser: IAppuser = { id: 89327 };
        blockuser.blockeduser = blockeduser;
        const blockinguser: IAppuser = { id: 38380 };
        blockuser.blockinguser = blockinguser;
        const cblockeduser: ICommunity = { id: 48968 };
        blockuser.cblockeduser = cblockeduser;
        const cblockinguser: ICommunity = { id: 25126 };
        blockuser.cblockinguser = cblockinguser;

        activatedRoute.data = of({ blockuser });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(blockuser));
        expect(comp.appusersSharedCollection).toContain(blockeduser);
        expect(comp.appusersSharedCollection).toContain(blockinguser);
        expect(comp.communitiesSharedCollection).toContain(cblockeduser);
        expect(comp.communitiesSharedCollection).toContain(cblockinguser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Blockuser>>();
        const blockuser = { id: 123 };
        jest.spyOn(blockuserService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ blockuser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: blockuser }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(blockuserService.update).toHaveBeenCalledWith(blockuser);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Blockuser>>();
        const blockuser = new Blockuser();
        jest.spyOn(blockuserService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ blockuser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: blockuser }));
        saveSubject.complete();

        // THEN
        expect(blockuserService.create).toHaveBeenCalledWith(blockuser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Blockuser>>();
        const blockuser = { id: 123 };
        jest.spyOn(blockuserService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ blockuser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(blockuserService.update).toHaveBeenCalledWith(blockuser);
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
