import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVtopic, Vtopic } from '../vtopic.model';

import { VtopicService } from './vtopic.service';

describe('Service Tests', () => {
  describe('Vtopic Service', () => {
    let service: VtopicService;
    let httpMock: HttpTestingController;
    let elemDefault: IVtopic;
    let expectedResult: IVtopic | IVtopic[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(VtopicService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
        vtopicTitle: 'AAAAAAA',
        vtopicDescription: 'AAAAAAA',
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

      it('should create a Vtopic', () => {
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

        service.create(new Vtopic()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Vtopic', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            vtopicTitle: 'BBBBBB',
            vtopicDescription: 'BBBBBB',
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

      it('should partial update a Vtopic', () => {
        const patchObject = Object.assign(
          {
            vtopicTitle: 'BBBBBB',
            vtopicDescription: 'BBBBBB',
          },
          new Vtopic()
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

      it('should return a list of Vtopic', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            vtopicTitle: 'BBBBBB',
            vtopicDescription: 'BBBBBB',
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

      it('should delete a Vtopic', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addVtopicToCollectionIfMissing', () => {
        it('should add a Vtopic to an empty array', () => {
          const vtopic: IVtopic = { id: 123 };
          expectedResult = service.addVtopicToCollectionIfMissing([], vtopic);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vtopic);
        });

        it('should not add a Vtopic to an array that contains it', () => {
          const vtopic: IVtopic = { id: 123 };
          const vtopicCollection: IVtopic[] = [
            {
              ...vtopic,
            },
            { id: 456 },
          ];
          expectedResult = service.addVtopicToCollectionIfMissing(vtopicCollection, vtopic);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Vtopic to an array that doesn't contain it", () => {
          const vtopic: IVtopic = { id: 123 };
          const vtopicCollection: IVtopic[] = [{ id: 456 }];
          expectedResult = service.addVtopicToCollectionIfMissing(vtopicCollection, vtopic);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vtopic);
        });

        it('should add only unique Vtopic to an array', () => {
          const vtopicArray: IVtopic[] = [{ id: 123 }, { id: 456 }, { id: 48494 }];
          const vtopicCollection: IVtopic[] = [{ id: 123 }];
          expectedResult = service.addVtopicToCollectionIfMissing(vtopicCollection, ...vtopicArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const vtopic: IVtopic = { id: 123 };
          const vtopic2: IVtopic = { id: 456 };
          expectedResult = service.addVtopicToCollectionIfMissing([], vtopic, vtopic2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vtopic);
          expect(expectedResult).toContain(vtopic2);
        });

        it('should accept null and undefined values', () => {
          const vtopic: IVtopic = { id: 123 };
          expectedResult = service.addVtopicToCollectionIfMissing([], null, vtopic, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vtopic);
        });

        it('should return initial array if no Vtopic is added', () => {
          const vtopicCollection: IVtopic[] = [{ id: 123 }];
          expectedResult = service.addVtopicToCollectionIfMissing(vtopicCollection, undefined, null);
          expect(expectedResult).toEqual(vtopicCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
