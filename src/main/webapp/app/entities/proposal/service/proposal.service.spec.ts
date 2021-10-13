import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ProposalType } from 'app/entities/enumerations/proposal-type.model';
import { ProposalRole } from 'app/entities/enumerations/proposal-role.model';
import { IProposal, Proposal } from '../proposal.model';

import { ProposalService } from './proposal.service';

describe('Service Tests', () => {
  describe('Proposal Service', () => {
    let service: ProposalService;
    let httpMock: HttpTestingController;
    let elemDefault: IProposal;
    let expectedResult: IProposal | IProposal[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ProposalService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
        proposalName: 'AAAAAAA',
        proposalType: ProposalType.STUDY,
        proposalRole: ProposalRole.USER,
        releaseDate: currentDate,
        isOpen: false,
        isAccepted: false,
        isPaid: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            releaseDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Proposal', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            releaseDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
            releaseDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Proposal()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Proposal', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            proposalName: 'BBBBBB',
            proposalType: 'BBBBBB',
            proposalRole: 'BBBBBB',
            releaseDate: currentDate.format(DATE_TIME_FORMAT),
            isOpen: true,
            isAccepted: true,
            isPaid: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
            releaseDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Proposal', () => {
        const patchObject = Object.assign(
          {
            proposalName: 'BBBBBB',
            releaseDate: currentDate.format(DATE_TIME_FORMAT),
            isOpen: true,
          },
          new Proposal()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            creationDate: currentDate,
            releaseDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Proposal', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            proposalName: 'BBBBBB',
            proposalType: 'BBBBBB',
            proposalRole: 'BBBBBB',
            releaseDate: currentDate.format(DATE_TIME_FORMAT),
            isOpen: true,
            isAccepted: true,
            isPaid: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
            releaseDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Proposal', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addProposalToCollectionIfMissing', () => {
        it('should add a Proposal to an empty array', () => {
          const proposal: IProposal = { id: 123 };
          expectedResult = service.addProposalToCollectionIfMissing([], proposal);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(proposal);
        });

        it('should not add a Proposal to an array that contains it', () => {
          const proposal: IProposal = { id: 123 };
          const proposalCollection: IProposal[] = [
            {
              ...proposal,
            },
            { id: 456 },
          ];
          expectedResult = service.addProposalToCollectionIfMissing(proposalCollection, proposal);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Proposal to an array that doesn't contain it", () => {
          const proposal: IProposal = { id: 123 };
          const proposalCollection: IProposal[] = [{ id: 456 }];
          expectedResult = service.addProposalToCollectionIfMissing(proposalCollection, proposal);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(proposal);
        });

        it('should add only unique Proposal to an array', () => {
          const proposalArray: IProposal[] = [{ id: 123 }, { id: 456 }, { id: 53339 }];
          const proposalCollection: IProposal[] = [{ id: 123 }];
          expectedResult = service.addProposalToCollectionIfMissing(proposalCollection, ...proposalArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const proposal: IProposal = { id: 123 };
          const proposal2: IProposal = { id: 456 };
          expectedResult = service.addProposalToCollectionIfMissing([], proposal, proposal2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(proposal);
          expect(expectedResult).toContain(proposal2);
        });

        it('should accept null and undefined values', () => {
          const proposal: IProposal = { id: 123 };
          expectedResult = service.addProposalToCollectionIfMissing([], null, proposal, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(proposal);
        });

        it('should return initial array if no Proposal is added', () => {
          const proposalCollection: IProposal[] = [{ id: 123 }];
          expectedResult = service.addProposalToCollectionIfMissing(proposalCollection, undefined, null);
          expect(expectedResult).toEqual(proposalCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
