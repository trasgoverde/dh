jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VquestionService } from '../service/vquestion.service';
import { IVquestion, Vquestion } from '../vquestion.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { IVtopic } from 'app/entities/vtopic/vtopic.model';
import { VtopicService } from 'app/entities/vtopic/service/vtopic.service';

import { VquestionUpdateComponent } from './vquestion-update.component';

describe('Component Tests', () => {
  describe('Vquestion Management Update Component', () => {
    let comp: VquestionUpdateComponent;
    let fixture: ComponentFixture<VquestionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let vquestionService: VquestionService;
    let appuserService: AppuserService;
    let vtopicService: VtopicService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VquestionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VquestionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VquestionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      vquestionService = TestBed.inject(VquestionService);
      appuserService = TestBed.inject(AppuserService);
      vtopicService = TestBed.inject(VtopicService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Appuser query and add missing value', () => {
        const vquestion: IVquestion = { id: 456 };
        const appuser: IAppuser = { id: 53831 };
        vquestion.appuser = appuser;

        const appuserCollection: IAppuser[] = [{ id: 44977 }];
        jest.spyOn(appuserService, 'query').mockReturnValue(of(new HttpResponse({ body: appuserCollection })));
        const additionalAppusers = [appuser];
        const expectedCollection: IAppuser[] = [...additionalAppusers, ...appuserCollection];
        jest.spyOn(appuserService, 'addAppuserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ vquestion });
        comp.ngOnInit();

        expect(appuserService.query).toHaveBeenCalled();
        expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(appuserCollection, ...additionalAppusers);
        expect(comp.appusersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Vtopic query and add missing value', () => {
        const vquestion: IVquestion = { id: 456 };
        const vtopic: IVtopic = { id: 52844 };
        vquestion.vtopic = vtopic;

        const vtopicCollection: IVtopic[] = [{ id: 24853 }];
        jest.spyOn(vtopicService, 'query').mockReturnValue(of(new HttpResponse({ body: vtopicCollection })));
        const additionalVtopics = [vtopic];
        const expectedCollection: IVtopic[] = [...additionalVtopics, ...vtopicCollection];
        jest.spyOn(vtopicService, 'addVtopicToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ vquestion });
        comp.ngOnInit();

        expect(vtopicService.query).toHaveBeenCalled();
        expect(vtopicService.addVtopicToCollectionIfMissing).toHaveBeenCalledWith(vtopicCollection, ...additionalVtopics);
        expect(comp.vtopicsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const vquestion: IVquestion = { id: 456 };
        const appuser: IAppuser = { id: 72020 };
        vquestion.appuser = appuser;
        const vtopic: IVtopic = { id: 89483 };
        vquestion.vtopic = vtopic;

        activatedRoute.data = of({ vquestion });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(vquestion));
        expect(comp.appusersSharedCollection).toContain(appuser);
        expect(comp.vtopicsSharedCollection).toContain(vtopic);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vquestion>>();
        const vquestion = { id: 123 };
        jest.spyOn(vquestionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vquestion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vquestion }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(vquestionService.update).toHaveBeenCalledWith(vquestion);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vquestion>>();
        const vquestion = new Vquestion();
        jest.spyOn(vquestionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vquestion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vquestion }));
        saveSubject.complete();

        // THEN
        expect(vquestionService.create).toHaveBeenCalledWith(vquestion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vquestion>>();
        const vquestion = { id: 123 };
        jest.spyOn(vquestionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vquestion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(vquestionService.update).toHaveBeenCalledWith(vquestion);
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

      describe('trackVtopicById', () => {
        it('Should return tracked Vtopic primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVtopicById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
