import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVanswer, Vanswer } from '../vanswer.model';

import { VanswerService } from './vanswer.service';

describe('Service Tests', () => {
  describe('Vanswer Service', () => {
    let service: VanswerService;
    let httpMock: HttpTestingController;
    let elemDefault: IVanswer;
    let expectedResult: IVanswer | IVanswer[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(VanswerService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
        urlVanswer: 'AAAAAAA',
        accepted: false,
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

      it('should create a Vanswer', () => {
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

        service.create(new Vanswer()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Vanswer', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            urlVanswer: 'BBBBBB',
            accepted: true,
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

      it('should partial update a Vanswer', () => {
        const patchObject = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            urlVanswer: 'BBBBBB',
            accepted: true,
          },
          new Vanswer()
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

      it('should return a list of Vanswer', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            urlVanswer: 'BBBBBB',
            accepted: true,
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

      it('should delete a Vanswer', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addVanswerToCollectionIfMissing', () => {
        it('should add a Vanswer to an empty array', () => {
          const vanswer: IVanswer = { id: 123 };
          expectedResult = service.addVanswerToCollectionIfMissing([], vanswer);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vanswer);
        });

        it('should not add a Vanswer to an array that contains it', () => {
          const vanswer: IVanswer = { id: 123 };
          const vanswerCollection: IVanswer[] = [
            {
              ...vanswer,
            },
            { id: 456 },
          ];
          expectedResult = service.addVanswerToCollectionIfMissing(vanswerCollection, vanswer);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Vanswer to an array that doesn't contain it", () => {
          const vanswer: IVanswer = { id: 123 };
          const vanswerCollection: IVanswer[] = [{ id: 456 }];
          expectedResult = service.addVanswerToCollectionIfMissing(vanswerCollection, vanswer);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vanswer);
        });

        it('should add only unique Vanswer to an array', () => {
          const vanswerArray: IVanswer[] = [{ id: 123 }, { id: 456 }, { id: 90349 }];
          const vanswerCollection: IVanswer[] = [{ id: 123 }];
          expectedResult = service.addVanswerToCollectionIfMissing(vanswerCollection, ...vanswerArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const vanswer: IVanswer = { id: 123 };
          const vanswer2: IVanswer = { id: 456 };
          expectedResult = service.addVanswerToCollectionIfMissing([], vanswer, vanswer2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vanswer);
          expect(expectedResult).toContain(vanswer2);
        });

        it('should accept null and undefined values', () => {
          const vanswer: IVanswer = { id: 123 };
          expectedResult = service.addVanswerToCollectionIfMissing([], null, vanswer, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vanswer);
        });

        it('should return initial array if no Vanswer is added', () => {
          const vanswerCollection: IVanswer[] = [{ id: 123 }];
          expectedResult = service.addVanswerToCollectionIfMissing(vanswerCollection, undefined, null);
          expect(expectedResult).toEqual(vanswerCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
