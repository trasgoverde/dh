jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NewsletterService } from '../service/newsletter.service';
import { INewsletter, Newsletter } from '../newsletter.model';

import { NewsletterUpdateComponent } from './newsletter-update.component';

describe('Component Tests', () => {
  describe('Newsletter Management Update Component', () => {
    let comp: NewsletterUpdateComponent;
    let fixture: ComponentFixture<NewsletterUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let newsletterService: NewsletterService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NewsletterUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(NewsletterUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NewsletterUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      newsletterService = TestBed.inject(NewsletterService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const newsletter: INewsletter = { id: 456 };

        activatedRoute.data = of({ newsletter });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(newsletter));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Newsletter>>();
        const newsletter = { id: 123 };
        jest.spyOn(newsletterService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ newsletter });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: newsletter }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(newsletterService.update).toHaveBeenCalledWith(newsletter);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Newsletter>>();
        const newsletter = new Newsletter();
        jest.spyOn(newsletterService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ newsletter });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: newsletter }));
        saveSubject.complete();

        // THEN
        expect(newsletterService.create).toHaveBeenCalledWith(newsletter);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Newsletter>>();
        const newsletter = { id: 123 };
        jest.spyOn(newsletterService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ newsletter });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(newsletterService.update).toHaveBeenCalledWith(newsletter);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
