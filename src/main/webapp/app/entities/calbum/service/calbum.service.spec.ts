import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICalbum, Calbum } from '../calbum.model';

import { CalbumService } from './calbum.service';

describe('Service Tests', () => {
  describe('Calbum Service', () => {
    let service: CalbumService;
    let httpMock: HttpTestingController;
    let elemDefault: ICalbum;
    let expectedResult: ICalbum | ICalbum[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CalbumService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
        title: 'AAAAAAA',
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

      it('should create a Calbum', () => {
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

        service.create(new Calbum()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Calbum', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            title: 'BBBBBB',
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

      it('should partial update a Calbum', () => {
        const patchObject = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            title: 'BBBBBB',
          },
          new Calbum()
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

      it('should return a list of Calbum', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            title: 'BBBBBB',
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

      it('should delete a Calbum', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCalbumToCollectionIfMissing', () => {
        it('should add a Calbum to an empty array', () => {
          const calbum: ICalbum = { id: 123 };
          expectedResult = service.addCalbumToCollectionIfMissing([], calbum);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(calbum);
        });

        it('should not add a Calbum to an array that contains it', () => {
          const calbum: ICalbum = { id: 123 };
          const calbumCollection: ICalbum[] = [
            {
              ...calbum,
            },
            { id: 456 },
          ];
          expectedResult = service.addCalbumToCollectionIfMissing(calbumCollection, calbum);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Calbum to an array that doesn't contain it", () => {
          const calbum: ICalbum = { id: 123 };
          const calbumCollection: ICalbum[] = [{ id: 456 }];
          expectedResult = service.addCalbumToCollectionIfMissing(calbumCollection, calbum);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(calbum);
        });

        it('should add only unique Calbum to an array', () => {
          const calbumArray: ICalbum[] = [{ id: 123 }, { id: 456 }, { id: 25184 }];
          const calbumCollection: ICalbum[] = [{ id: 123 }];
          expectedResult = service.addCalbumToCollectionIfMissing(calbumCollection, ...calbumArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const calbum: ICalbum = { id: 123 };
          const calbum2: ICalbum = { id: 456 };
          expectedResult = service.addCalbumToCollectionIfMissing([], calbum, calbum2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(calbum);
          expect(expectedResult).toContain(calbum2);
        });

        it('should accept null and undefined values', () => {
          const calbum: ICalbum = { id: 123 };
          expectedResult = service.addCalbumToCollectionIfMissing([], null, calbum, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(calbum);
        });

        it('should return initial array if no Calbum is added', () => {
          const calbumCollection: ICalbum[] = [{ id: 123 }];
          expectedResult = service.addCalbumToCollectionIfMissing(calbumCollection, undefined, null);
          expect(expectedResult).toEqual(calbumCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
