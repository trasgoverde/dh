jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VtopicService } from '../service/vtopic.service';
import { IVtopic, Vtopic } from '../vtopic.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';

import { VtopicUpdateComponent } from './vtopic-update.component';

describe('Component Tests', () => {
  describe('Vtopic Management Update Component', () => {
    let comp: VtopicUpdateComponent;
    let fixture: ComponentFixture<VtopicUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let vtopicService: VtopicService;
    let appuserService: AppuserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VtopicUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VtopicUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VtopicUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      vtopicService = TestBed.inject(VtopicService);
      appuserService = TestBed.inject(AppuserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Appuser query and add missing value', () => {
        const vtopic: IVtopic = { id: 456 };
        const appuser: IAppuser = { id: 56726 };
        vtopic.appuser = appuser;

        const appuserCollection: IAppuser[] = [{ id: 8515 }];
        jest.spyOn(appuserService, 'query').mockReturnValue(of(new HttpResponse({ body: appuserCollection })));
        const additionalAppusers = [appuser];
        const expectedCollection: IAppuser[] = [...additionalAppusers, ...appuserCollection];
        jest.spyOn(appuserService, 'addAppuserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ vtopic });
        comp.ngOnInit();

        expect(appuserService.query).toHaveBeenCalled();
        expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(appuserCollection, ...additionalAppusers);
        expect(comp.appusersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const vtopic: IVtopic = { id: 456 };
        const appuser: IAppuser = { id: 15182 };
        vtopic.appuser = appuser;

        activatedRoute.data = of({ vtopic });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(vtopic));
        expect(comp.appusersSharedCollection).toContain(appuser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vtopic>>();
        const vtopic = { id: 123 };
        jest.spyOn(vtopicService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vtopic });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vtopic }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(vtopicService.update).toHaveBeenCalledWith(vtopic);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vtopic>>();
        const vtopic = new Vtopic();
        jest.spyOn(vtopicService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vtopic });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vtopic }));
        saveSubject.complete();

        // THEN
        expect(vtopicService.create).toHaveBeenCalledWith(vtopic);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vtopic>>();
        const vtopic = { id: 123 };
        jest.spyOn(vtopicService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vtopic });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(vtopicService.update).toHaveBeenCalledWith(vtopic);
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
    });
  });
});
