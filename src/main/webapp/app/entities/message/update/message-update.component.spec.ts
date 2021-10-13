jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MessageService } from '../service/message.service';
import { IMessage, Message } from '../message.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';

import { MessageUpdateComponent } from './message-update.component';

describe('Component Tests', () => {
  describe('Message Management Update Component', () => {
    let comp: MessageUpdateComponent;
    let fixture: ComponentFixture<MessageUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let messageService: MessageService;
    let appuserService: AppuserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MessageUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MessageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MessageUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      messageService = TestBed.inject(MessageService);
      appuserService = TestBed.inject(AppuserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Appuser query and add missing value', () => {
        const message: IMessage = { id: 456 };
        const sender: IAppuser = { id: 45270 };
        message.sender = sender;
        const receiver: IAppuser = { id: 76006 };
        message.receiver = receiver;

        const appuserCollection: IAppuser[] = [{ id: 70355 }];
        jest.spyOn(appuserService, 'query').mockReturnValue(of(new HttpResponse({ body: appuserCollection })));
        const additionalAppusers = [sender, receiver];
        const expectedCollection: IAppuser[] = [...additionalAppusers, ...appuserCollection];
        jest.spyOn(appuserService, 'addAppuserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ message });
        comp.ngOnInit();

        expect(appuserService.query).toHaveBeenCalled();
        expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(appuserCollection, ...additionalAppusers);
        expect(comp.appusersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const message: IMessage = { id: 456 };
        const sender: IAppuser = { id: 86308 };
        message.sender = sender;
        const receiver: IAppuser = { id: 16862 };
        message.receiver = receiver;

        activatedRoute.data = of({ message });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(message));
        expect(comp.appusersSharedCollection).toContain(sender);
        expect(comp.appusersSharedCollection).toContain(receiver);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Message>>();
        const message = { id: 123 };
        jest.spyOn(messageService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ message });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: message }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(messageService.update).toHaveBeenCalledWith(message);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Message>>();
        const message = new Message();
        jest.spyOn(messageService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ message });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: message }));
        saveSubject.complete();

        // THEN
        expect(messageService.create).toHaveBeenCalledWith(message);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Message>>();
        const message = { id: 123 };
        jest.spyOn(messageService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ message });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(messageService.update).toHaveBeenCalledWith(message);
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
