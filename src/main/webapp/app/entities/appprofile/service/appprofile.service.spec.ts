import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Gender } from 'app/entities/enumerations/gender.model';
import { CivilStatus } from 'app/entities/enumerations/civil-status.model';
import { Purpose } from 'app/entities/enumerations/purpose.model';
import { Physical } from 'app/entities/enumerations/physical.model';
import { Religion } from 'app/entities/enumerations/religion.model';
import { EthnicGroup } from 'app/entities/enumerations/ethnic-group.model';
import { Studies } from 'app/entities/enumerations/studies.model';
import { Eyes } from 'app/entities/enumerations/eyes.model';
import { Smoker } from 'app/entities/enumerations/smoker.model';
import { Children } from 'app/entities/enumerations/children.model';
import { FutureChildren } from 'app/entities/enumerations/future-children.model';
import { IAppprofile, Appprofile } from '../appprofile.model';

import { AppprofileService } from './appprofile.service';

describe('Service Tests', () => {
  describe('Appprofile Service', () => {
    let service: AppprofileService;
    let httpMock: HttpTestingController;
    let elemDefault: IAppprofile;
    let expectedResult: IAppprofile | IAppprofile[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AppprofileService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
        gender: Gender.MALE,
        phone: 'AAAAAAA',
        bio: 'AAAAAAA',
        facebook: 'AAAAAAA',
        twitter: 'AAAAAAA',
        linkedin: 'AAAAAAA',
        instagram: 'AAAAAAA',
        googlePlus: 'AAAAAAA',
        birthdate: currentDate,
        civilStatus: CivilStatus.NA,
        lookingFor: Gender.MALE,
        purpose: Purpose.NOT_INTERESTED,
        physical: Physical.NA,
        religion: Religion.NA,
        ethnicGroup: EthnicGroup.NA,
        studies: Studies.NA,
        sibblings: 0,
        eyes: Eyes.NA,
        smoker: Smoker.NA,
        children: Children.NA,
        futureChildren: FutureChildren.NA,
        pet: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            birthdate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Appprofile', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            birthdate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
            birthdate: currentDate,
          },
          returnedFromService
        );

        service.create(new Appprofile()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Appprofile', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            gender: 'BBBBBB',
            phone: 'BBBBBB',
            bio: 'BBBBBB',
            facebook: 'BBBBBB',
            twitter: 'BBBBBB',
            linkedin: 'BBBBBB',
            instagram: 'BBBBBB',
            googlePlus: 'BBBBBB',
            birthdate: currentDate.format(DATE_TIME_FORMAT),
            civilStatus: 'BBBBBB',
            lookingFor: 'BBBBBB',
            purpose: 'BBBBBB',
            physical: 'BBBBBB',
            religion: 'BBBBBB',
            ethnicGroup: 'BBBBBB',
            studies: 'BBBBBB',
            sibblings: 1,
            eyes: 'BBBBBB',
            smoker: 'BBBBBB',
            children: 'BBBBBB',
            futureChildren: 'BBBBBB',
            pet: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
            birthdate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Appprofile', () => {
        const patchObject = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            phone: 'BBBBBB',
            googlePlus: 'BBBBBB',
            birthdate: currentDate.format(DATE_TIME_FORMAT),
            civilStatus: 'BBBBBB',
            lookingFor: 'BBBBBB',
            purpose: 'BBBBBB',
            ethnicGroup: 'BBBBBB',
            children: 'BBBBBB',
            pet: true,
          },
          new Appprofile()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            creationDate: currentDate,
            birthdate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Appprofile', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            gender: 'BBBBBB',
            phone: 'BBBBBB',
            bio: 'BBBBBB',
            facebook: 'BBBBBB',
            twitter: 'BBBBBB',
            linkedin: 'BBBBBB',
            instagram: 'BBBBBB',
            googlePlus: 'BBBBBB',
            birthdate: currentDate.format(DATE_TIME_FORMAT),
            civilStatus: 'BBBBBB',
            lookingFor: 'BBBBBB',
            purpose: 'BBBBBB',
            physical: 'BBBBBB',
            religion: 'BBBBBB',
            ethnicGroup: 'BBBBBB',
            studies: 'BBBBBB',
            sibblings: 1,
            eyes: 'BBBBBB',
            smoker: 'BBBBBB',
            children: 'BBBBBB',
            futureChildren: 'BBBBBB',
            pet: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
            birthdate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Appprofile', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAppprofileToCollectionIfMissing', () => {
        it('should add a Appprofile to an empty array', () => {
          const appprofile: IAppprofile = { id: 123 };
          expectedResult = service.addAppprofileToCollectionIfMissing([], appprofile);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(appprofile);
        });

        it('should not add a Appprofile to an array that contains it', () => {
          const appprofile: IAppprofile = { id: 123 };
          const appprofileCollection: IAppprofile[] = [
            {
              ...appprofile,
            },
            { id: 456 },
          ];
          expectedResult = service.addAppprofileToCollectionIfMissing(appprofileCollection, appprofile);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Appprofile to an array that doesn't contain it", () => {
          const appprofile: IAppprofile = { id: 123 };
          const appprofileCollection: IAppprofile[] = [{ id: 456 }];
          expectedResult = service.addAppprofileToCollectionIfMissing(appprofileCollection, appprofile);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(appprofile);
        });

        it('should add only unique Appprofile to an array', () => {
          const appprofileArray: IAppprofile[] = [{ id: 123 }, { id: 456 }, { id: 27862 }];
          const appprofileCollection: IAppprofile[] = [{ id: 123 }];
          expectedResult = service.addAppprofileToCollectionIfMissing(appprofileCollection, ...appprofileArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const appprofile: IAppprofile = { id: 123 };
          const appprofile2: IAppprofile = { id: 456 };
          expectedResult = service.addAppprofileToCollectionIfMissing([], appprofile, appprofile2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(appprofile);
          expect(expectedResult).toContain(appprofile2);
        });

        it('should accept null and undefined values', () => {
          const appprofile: IAppprofile = { id: 123 };
          expectedResult = service.addAppprofileToCollectionIfMissing([], null, appprofile, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(appprofile);
        });

        it('should return initial array if no Appprofile is added', () => {
          const appprofileCollection: IAppprofile[] = [{ id: 123 }];
          expectedResult = service.addAppprofileToCollectionIfMissing(appprofileCollection, undefined, null);
          expect(expectedResult).toEqual(appprofileCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
