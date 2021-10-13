jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VthumbService } from '../service/vthumb.service';
import { IVthumb, Vthumb } from '../vthumb.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { IVquestion } from 'app/entities/vquestion/vquestion.model';
import { VquestionService } from 'app/entities/vquestion/service/vquestion.service';
import { IVanswer } from 'app/entities/vanswer/vanswer.model';
import { VanswerService } from 'app/entities/vanswer/service/vanswer.service';

import { VthumbUpdateComponent } from './vthumb-update.component';

describe('Component Tests', () => {
  describe('Vthumb Management Update Component', () => {
    let comp: VthumbUpdateComponent;
    let fixture: ComponentFixture<VthumbUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let vthumbService: VthumbService;
    let appuserService: AppuserService;
    let vquestionService: VquestionService;
    let vanswerService: VanswerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VthumbUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VthumbUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VthumbUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      vthumbService = TestBed.inject(VthumbService);
      appuserService = TestBed.inject(AppuserService);
      vquestionService = TestBed.inject(VquestionService);
      vanswerService = TestBed.inject(VanswerService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Appuser query and add missing value', () => {
        const vthumb: IVthumb = { id: 456 };
        const appuser: IAppuser = { id: 40826 };
        vthumb.appuser = appuser;

        const appuserCollection: IAppuser[] = [{ id: 26718 }];
        jest.spyOn(appuserService, 'query').mockReturnValue(of(new HttpResponse({ body: appuserCollection })));
        const additionalAppusers = [appuser];
        const expectedCollection: IAppuser[] = [...additionalAppusers, ...appuserCollection];
        jest.spyOn(appuserService, 'addAppuserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ vthumb });
        comp.ngOnInit();

        expect(appuserService.query).toHaveBeenCalled();
        expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(appuserCollection, ...additionalAppusers);
        expect(comp.appusersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Vquestion query and add missing value', () => {
        const vthumb: IVthumb = { id: 456 };
        const vquestion: IVquestion = { id: 81632 };
        vthumb.vquestion = vquestion;

        const vquestionCollection: IVquestion[] = [{ id: 89302 }];
        jest.spyOn(vquestionService, 'query').mockReturnValue(of(new HttpResponse({ body: vquestionCollection })));
        const additionalVquestions = [vquestion];
        const expectedCollection: IVquestion[] = [...additionalVquestions, ...vquestionCollection];
        jest.spyOn(vquestionService, 'addVquestionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ vthumb });
        comp.ngOnInit();

        expect(vquestionService.query).toHaveBeenCalled();
        expect(vquestionService.addVquestionToCollectionIfMissing).toHaveBeenCalledWith(vquestionCollection, ...additionalVquestions);
        expect(comp.vquestionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Vanswer query and add missing value', () => {
        const vthumb: IVthumb = { id: 456 };
        const vanswer: IVanswer = { id: 75465 };
        vthumb.vanswer = vanswer;

        const vanswerCollection: IVanswer[] = [{ id: 25636 }];
        jest.spyOn(vanswerService, 'query').mockReturnValue(of(new HttpResponse({ body: vanswerCollection })));
        const additionalVanswers = [vanswer];
        const expectedCollection: IVanswer[] = [...additionalVanswers, ...vanswerCollection];
        jest.spyOn(vanswerService, 'addVanswerToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ vthumb });
        comp.ngOnInit();

        expect(vanswerService.query).toHaveBeenCalled();
        expect(vanswerService.addVanswerToCollectionIfMissing).toHaveBeenCalledWith(vanswerCollection, ...additionalVanswers);
        expect(comp.vanswersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const vthumb: IVthumb = { id: 456 };
        const appuser: IAppuser = { id: 34929 };
        vthumb.appuser = appuser;
        const vquestion: IVquestion = { id: 34256 };
        vthumb.vquestion = vquestion;
        const vanswer: IVanswer = { id: 44425 };
        vthumb.vanswer = vanswer;

        activatedRoute.data = of({ vthumb });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(vthumb));
        expect(comp.appusersSharedCollection).toContain(appuser);
        expect(comp.vquestionsSharedCollection).toContain(vquestion);
        expect(comp.vanswersSharedCollection).toContain(vanswer);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vthumb>>();
        const vthumb = { id: 123 };
        jest.spyOn(vthumbService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vthumb });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vthumb }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(vthumbService.update).toHaveBeenCalledWith(vthumb);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vthumb>>();
        const vthumb = new Vthumb();
        jest.spyOn(vthumbService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vthumb });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vthumb }));
        saveSubject.complete();

        // THEN
        expect(vthumbService.create).toHaveBeenCalledWith(vthumb);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vthumb>>();
        const vthumb = { id: 123 };
        jest.spyOn(vthumbService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vthumb });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(vthumbService.update).toHaveBeenCalledWith(vthumb);
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

      describe('trackVanswerById', () => {
        it('Should return tracked Vanswer primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVanswerById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
