import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INewsletter, Newsletter } from '../newsletter.model';

import { NewsletterService } from './newsletter.service';

describe('Service Tests', () => {
  describe('Newsletter Service', () => {
    let service: NewsletterService;
    let httpMock: HttpTestingController;
    let elemDefault: INewsletter;
    let expectedResult: INewsletter | INewsletter[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(NewsletterService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
        email: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Newsletter', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Newsletter()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Newsletter', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            email: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Newsletter', () => {
        const patchObject = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
          },
          new Newsletter()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            creationDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Newsletter', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            email: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Newsletter', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addNewsletterToCollectionIfMissing', () => {
        it('should add a Newsletter to an empty array', () => {
          const newsletter: INewsletter = { id: 123 };
          expectedResult = service.addNewsletterToCollectionIfMissing([], newsletter);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(newsletter);
        });

        it('should not add a Newsletter to an array that contains it', () => {
          const newsletter: INewsletter = { id: 123 };
          const newsletterCollection: INewsletter[] = [
            {
              ...newsletter,
            },
            { id: 456 },
          ];
          expectedResult = service.addNewsletterToCollectionIfMissing(newsletterCollection, newsletter);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Newsletter to an array that doesn't contain it", () => {
          const newsletter: INewsletter = { id: 123 };
          const newsletterCollection: INewsletter[] = [{ id: 456 }];
          expectedResult = service.addNewsletterToCollectionIfMissing(newsletterCollection, newsletter);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(newsletter);
        });

        it('should add only unique Newsletter to an array', () => {
          const newsletterArray: INewsletter[] = [{ id: 123 }, { id: 456 }, { id: 48292 }];
          const newsletterCollection: INewsletter[] = [{ id: 123 }];
          expectedResult = service.addNewsletterToCollectionIfMissing(newsletterCollection, ...newsletterArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const newsletter: INewsletter = { id: 123 };
          const newsletter2: INewsletter = { id: 456 };
          expectedResult = service.addNewsletterToCollectionIfMissing([], newsletter, newsletter2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(newsletter);
          expect(expectedResult).toContain(newsletter2);
        });

        it('should accept null and undefined values', () => {
          const newsletter: INewsletter = { id: 123 };
          expectedResult = service.addNewsletterToCollectionIfMissing([], null, newsletter, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(newsletter);
        });

        it('should return initial array if no Newsletter is added', () => {
          const newsletterCollection: INewsletter[] = [{ id: 123 }];
          expectedResult = service.addNewsletterToCollectionIfMissing(newsletterCollection, undefined, null);
          expect(expectedResult).toEqual(newsletterCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
