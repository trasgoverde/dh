import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICmessage, Cmessage } from '../cmessage.model';

import { CmessageService } from './cmessage.service';

describe('Service Tests', () => {
  describe('Cmessage Service', () => {
    let service: CmessageService;
    let httpMock: HttpTestingController;
    let elemDefault: ICmessage;
    let expectedResult: ICmessage | ICmessage[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CmessageService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
        messageText: 'AAAAAAA',
        isDelivered: false,
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

      it('should create a Cmessage', () => {
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

        service.create(new Cmessage()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Cmessage', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            messageText: 'BBBBBB',
            isDelivered: true,
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

      it('should partial update a Cmessage', () => {
        const patchObject = Object.assign(
          {
            messageText: 'BBBBBB',
            isDelivered: true,
          },
          new Cmessage()
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

      it('should return a list of Cmessage', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            messageText: 'BBBBBB',
            isDelivered: true,
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

      it('should delete a Cmessage', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCmessageToCollectionIfMissing', () => {
        it('should add a Cmessage to an empty array', () => {
          const cmessage: ICmessage = { id: 123 };
          expectedResult = service.addCmessageToCollectionIfMissing([], cmessage);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cmessage);
        });

        it('should not add a Cmessage to an array that contains it', () => {
          const cmessage: ICmessage = { id: 123 };
          const cmessageCollection: ICmessage[] = [
            {
              ...cmessage,
            },
            { id: 456 },
          ];
          expectedResult = service.addCmessageToCollectionIfMissing(cmessageCollection, cmessage);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Cmessage to an array that doesn't contain it", () => {
          const cmessage: ICmessage = { id: 123 };
          const cmessageCollection: ICmessage[] = [{ id: 456 }];
          expectedResult = service.addCmessageToCollectionIfMissing(cmessageCollection, cmessage);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cmessage);
        });

        it('should add only unique Cmessage to an array', () => {
          const cmessageArray: ICmessage[] = [{ id: 123 }, { id: 456 }, { id: 65831 }];
          const cmessageCollection: ICmessage[] = [{ id: 123 }];
          expectedResult = service.addCmessageToCollectionIfMissing(cmessageCollection, ...cmessageArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const cmessage: ICmessage = { id: 123 };
          const cmessage2: ICmessage = { id: 456 };
          expectedResult = service.addCmessageToCollectionIfMissing([], cmessage, cmessage2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cmessage);
          expect(expectedResult).toContain(cmessage2);
        });

        it('should accept null and undefined values', () => {
          const cmessage: ICmessage = { id: 123 };
          expectedResult = service.addCmessageToCollectionIfMissing([], null, cmessage, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cmessage);
        });

        it('should return initial array if no Cmessage is added', () => {
          const cmessageCollection: ICmessage[] = [{ id: 123 }];
          expectedResult = service.addCmessageToCollectionIfMissing(cmessageCollection, undefined, null);
          expect(expectedResult).toEqual(cmessageCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
