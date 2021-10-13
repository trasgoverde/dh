import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVthumb, Vthumb } from '../vthumb.model';

import { VthumbService } from './vthumb.service';

describe('Service Tests', () => {
  describe('Vthumb Service', () => {
    let service: VthumbService;
    let httpMock: HttpTestingController;
    let elemDefault: IVthumb;
    let expectedResult: IVthumb | IVthumb[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(VthumbService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
        vthumbUp: false,
        vthumbDown: false,
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

      it('should create a Vthumb', () => {
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

        service.create(new Vthumb()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Vthumb', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            vthumbUp: true,
            vthumbDown: true,
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

      it('should partial update a Vthumb', () => {
        const patchObject = Object.assign(
          {
            vthumbDown: true,
          },
          new Vthumb()
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

      it('should return a list of Vthumb', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            vthumbUp: true,
            vthumbDown: true,
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

      it('should delete a Vthumb', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addVthumbToCollectionIfMissing', () => {
        it('should add a Vthumb to an empty array', () => {
          const vthumb: IVthumb = { id: 123 };
          expectedResult = service.addVthumbToCollectionIfMissing([], vthumb);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vthumb);
        });

        it('should not add a Vthumb to an array that contains it', () => {
          const vthumb: IVthumb = { id: 123 };
          const vthumbCollection: IVthumb[] = [
            {
              ...vthumb,
            },
            { id: 456 },
          ];
          expectedResult = service.addVthumbToCollectionIfMissing(vthumbCollection, vthumb);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Vthumb to an array that doesn't contain it", () => {
          const vthumb: IVthumb = { id: 123 };
          const vthumbCollection: IVthumb[] = [{ id: 456 }];
          expectedResult = service.addVthumbToCollectionIfMissing(vthumbCollection, vthumb);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vthumb);
        });

        it('should add only unique Vthumb to an array', () => {
          const vthumbArray: IVthumb[] = [{ id: 123 }, { id: 456 }, { id: 58199 }];
          const vthumbCollection: IVthumb[] = [{ id: 123 }];
          expectedResult = service.addVthumbToCollectionIfMissing(vthumbCollection, ...vthumbArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const vthumb: IVthumb = { id: 123 };
          const vthumb2: IVthumb = { id: 456 };
          expectedResult = service.addVthumbToCollectionIfMissing([], vthumb, vthumb2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vthumb);
          expect(expectedResult).toContain(vthumb2);
        });

        it('should accept null and undefined values', () => {
          const vthumb: IVthumb = { id: 123 };
          expectedResult = service.addVthumbToCollectionIfMissing([], null, vthumb, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vthumb);
        });

        it('should return initial array if no Vthumb is added', () => {
          const vthumbCollection: IVthumb[] = [{ id: 123 }];
          expectedResult = service.addVthumbToCollectionIfMissing(vthumbCollection, undefined, null);
          expect(expectedResult).toEqual(vthumbCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
