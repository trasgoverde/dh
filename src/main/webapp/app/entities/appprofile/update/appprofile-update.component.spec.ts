jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AppprofileService } from '../service/appprofile.service';
import { IAppprofile, Appprofile } from '../appprofile.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';

import { AppprofileUpdateComponent } from './appprofile-update.component';

describe('Component Tests', () => {
  describe('Appprofile Management Update Component', () => {
    let comp: AppprofileUpdateComponent;
    let fixture: ComponentFixture<AppprofileUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let appprofileService: AppprofileService;
    let appuserService: AppuserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AppprofileUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AppprofileUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AppprofileUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      appprofileService = TestBed.inject(AppprofileService);
      appuserService = TestBed.inject(AppuserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call appuser query and add missing value', () => {
        const appprofile: IAppprofile = { id: 456 };
        const appuser: IAppuser = { id: 13637 };
        appprofile.appuser = appuser;

        const appuserCollection: IAppuser[] = [{ id: 52217 }];
        jest.spyOn(appuserService, 'query').mockReturnValue(of(new HttpResponse({ body: appuserCollection })));
        const expectedCollection: IAppuser[] = [appuser, ...appuserCollection];
        jest.spyOn(appuserService, 'addAppuserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ appprofile });
        comp.ngOnInit();

        expect(appuserService.query).toHaveBeenCalled();
        expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(appuserCollection, appuser);
        expect(comp.appusersCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const appprofile: IAppprofile = { id: 456 };
        const appuser: IAppuser = { id: 38640 };
        appprofile.appuser = appuser;

        activatedRoute.data = of({ appprofile });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(appprofile));
        expect(comp.appusersCollection).toContain(appuser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Appprofile>>();
        const appprofile = { id: 123 };
        jest.spyOn(appprofileService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ appprofile });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: appprofile }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(appprofileService.update).toHaveBeenCalledWith(appprofile);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Appprofile>>();
        const appprofile = new Appprofile();
        jest.spyOn(appprofileService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ appprofile });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: appprofile }));
        saveSubject.complete();

        // THEN
        expect(appprofileService.create).toHaveBeenCalledWith(appprofile);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Appprofile>>();
        const appprofile = { id: 123 };
        jest.spyOn(appprofileService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ appprofile });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(appprofileService.update).toHaveBeenCalledWith(appprofile);
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
