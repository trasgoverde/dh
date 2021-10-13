import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVquestion, Vquestion } from '../vquestion.model';

import { VquestionService } from './vquestion.service';

describe('Service Tests', () => {
  describe('Vquestion Service', () => {
    let service: VquestionService;
    let httpMock: HttpTestingController;
    let elemDefault: IVquestion;
    let expectedResult: IVquestion | IVquestion[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(VquestionService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
        vquestion: 'AAAAAAA',
        vquestionDescription: 'AAAAAAA',
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

      it('should create a Vquestion', () => {
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

        service.create(new Vquestion()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Vquestion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            vquestion: 'BBBBBB',
            vquestionDescription: 'BBBBBB',
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

      it('should partial update a Vquestion', () => {
        const patchObject = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            vquestion: 'BBBBBB',
          },
          new Vquestion()
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

      it('should return a list of Vquestion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            vquestion: 'BBBBBB',
            vquestionDescription: 'BBBBBB',
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

      it('should delete a Vquestion', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addVquestionToCollectionIfMissing', () => {
        it('should add a Vquestion to an empty array', () => {
          const vquestion: IVquestion = { id: 123 };
          expectedResult = service.addVquestionToCollectionIfMissing([], vquestion);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vquestion);
        });

        it('should not add a Vquestion to an array that contains it', () => {
          const vquestion: IVquestion = { id: 123 };
          const vquestionCollection: IVquestion[] = [
            {
              ...vquestion,
            },
            { id: 456 },
          ];
          expectedResult = service.addVquestionToCollectionIfMissing(vquestionCollection, vquestion);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Vquestion to an array that doesn't contain it", () => {
          const vquestion: IVquestion = { id: 123 };
          const vquestionCollection: IVquestion[] = [{ id: 456 }];
          expectedResult = service.addVquestionToCollectionIfMissing(vquestionCollection, vquestion);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vquestion);
        });

        it('should add only unique Vquestion to an array', () => {
          const vquestionArray: IVquestion[] = [{ id: 123 }, { id: 456 }, { id: 56212 }];
          const vquestionCollection: IVquestion[] = [{ id: 123 }];
          expectedResult = service.addVquestionToCollectionIfMissing(vquestionCollection, ...vquestionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const vquestion: IVquestion = { id: 123 };
          const vquestion2: IVquestion = { id: 456 };
          expectedResult = service.addVquestionToCollectionIfMissing([], vquestion, vquestion2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vquestion);
          expect(expectedResult).toContain(vquestion2);
        });

        it('should accept null and undefined values', () => {
          const vquestion: IVquestion = { id: 123 };
          expectedResult = service.addVquestionToCollectionIfMissing([], null, vquestion, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vquestion);
        });

        it('should return initial array if no Vquestion is added', () => {
          const vquestionCollection: IVquestion[] = [{ id: 123 }];
          expectedResult = service.addVquestionToCollectionIfMissing(vquestionCollection, undefined, null);
          expect(expectedResult).toEqual(vquestionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
