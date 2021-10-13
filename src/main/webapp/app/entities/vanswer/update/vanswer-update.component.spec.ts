jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VanswerService } from '../service/vanswer.service';
import { IVanswer, Vanswer } from '../vanswer.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { IVquestion } from 'app/entities/vquestion/vquestion.model';
import { VquestionService } from 'app/entities/vquestion/service/vquestion.service';

import { VanswerUpdateComponent } from './vanswer-update.component';

describe('Component Tests', () => {
  describe('Vanswer Management Update Component', () => {
    let comp: VanswerUpdateComponent;
    let fixture: ComponentFixture<VanswerUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let vanswerService: VanswerService;
    let appuserService: AppuserService;
    let vquestionService: VquestionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VanswerUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VanswerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VanswerUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      vanswerService = TestBed.inject(VanswerService);
      appuserService = TestBed.inject(AppuserService);
      vquestionService = TestBed.inject(VquestionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Appuser query and add missing value', () => {
        const vanswer: IVanswer = { id: 456 };
        const appuser: IAppuser = { id: 96888 };
        vanswer.appuser = appuser;

        const appuserCollection: IAppuser[] = [{ id: 28277 }];
        jest.spyOn(appuserService, 'query').mockReturnValue(of(new HttpResponse({ body: appuserCollection })));
        const additionalAppusers = [appuser];
        const expectedCollection: IAppuser[] = [...additionalAppusers, ...appuserCollection];
        jest.spyOn(appuserService, 'addAppuserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ vanswer });
        comp.ngOnInit();

        expect(appuserService.query).toHaveBeenCalled();
        expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(appuserCollection, ...additionalAppusers);
        expect(comp.appusersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Vquestion query and add missing value', () => {
        const vanswer: IVanswer = { id: 456 };
        const vquestion: IVquestion = { id: 22367 };
        vanswer.vquestion = vquestion;

        const vquestionCollection: IVquestion[] = [{ id: 76393 }];
        jest.spyOn(vquestionService, 'query').mockReturnValue(of(new HttpResponse({ body: vquestionCollection })));
        const additionalVquestions = [vquestion];
        const expectedCollection: IVquestion[] = [...additionalVquestions, ...vquestionCollection];
        jest.spyOn(vquestionService, 'addVquestionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ vanswer });
        comp.ngOnInit();

        expect(vquestionService.query).toHaveBeenCalled();
        expect(vquestionService.addVquestionToCollectionIfMissing).toHaveBeenCalledWith(vquestionCollection, ...additionalVquestions);
        expect(comp.vquestionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const vanswer: IVanswer = { id: 456 };
        const appuser: IAppuser = { id: 88311 };
        vanswer.appuser = appuser;
        const vquestion: IVquestion = { id: 93252 };
        vanswer.vquestion = vquestion;

        activatedRoute.data = of({ vanswer });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(vanswer));
        expect(comp.appusersSharedCollection).toContain(appuser);
        expect(comp.vquestionsSharedCollection).toContain(vquestion);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vanswer>>();
        const vanswer = { id: 123 };
        jest.spyOn(vanswerService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vanswer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vanswer }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(vanswerService.update).toHaveBeenCalledWith(vanswer);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vanswer>>();
        const vanswer = new Vanswer();
        jest.spyOn(vanswerService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vanswer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vanswer }));
        saveSubject.complete();

        // THEN
        expect(vanswerService.create).toHaveBeenCalledWith(vanswer);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vanswer>>();
        const vanswer = { id: 123 };
        jest.spyOn(vanswerService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vanswer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(vanswerService.update).toHaveBeenCalledWith(vanswer);
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

      describe('trackVquestionById', () => {
        it('Should return tracked Vquestion primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVquestionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
