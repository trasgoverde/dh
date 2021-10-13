import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProposalVote, ProposalVote } from '../proposal-vote.model';

import { ProposalVoteService } from './proposal-vote.service';

describe('Service Tests', () => {
  describe('ProposalVote Service', () => {
    let service: ProposalVoteService;
    let httpMock: HttpTestingController;
    let elemDefault: IProposalVote;
    let expectedResult: IProposalVote | IProposalVote[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ProposalVoteService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
        votePoints: 0,
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

      it('should create a ProposalVote', () => {
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

        service.create(new ProposalVote()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ProposalVote', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            votePoints: 1,
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

      it('should partial update a ProposalVote', () => {
        const patchObject = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            votePoints: 1,
          },
          new ProposalVote()
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

      it('should return a list of ProposalVote', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            votePoints: 1,
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

      it('should delete a ProposalVote', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addProposalVoteToCollectionIfMissing', () => {
        it('should add a ProposalVote to an empty array', () => {
          const proposalVote: IProposalVote = { id: 123 };
          expectedResult = service.addProposalVoteToCollectionIfMissing([], proposalVote);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(proposalVote);
        });

        it('should not add a ProposalVote to an array that contains it', () => {
          const proposalVote: IProposalVote = { id: 123 };
          const proposalVoteCollection: IProposalVote[] = [
            {
              ...proposalVote,
            },
            { id: 456 },
          ];
          expectedResult = service.addProposalVoteToCollectionIfMissing(proposalVoteCollection, proposalVote);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ProposalVote to an array that doesn't contain it", () => {
          const proposalVote: IProposalVote = { id: 123 };
          const proposalVoteCollection: IProposalVote[] = [{ id: 456 }];
          expectedResult = service.addProposalVoteToCollectionIfMissing(proposalVoteCollection, proposalVote);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(proposalVote);
        });

        it('should add only unique ProposalVote to an array', () => {
          const proposalVoteArray: IProposalVote[] = [{ id: 123 }, { id: 456 }, { id: 94685 }];
          const proposalVoteCollection: IProposalVote[] = [{ id: 123 }];
          expectedResult = service.addProposalVoteToCollectionIfMissing(proposalVoteCollection, ...proposalVoteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const proposalVote: IProposalVote = { id: 123 };
          const proposalVote2: IProposalVote = { id: 456 };
          expectedResult = service.addProposalVoteToCollectionIfMissing([], proposalVote, proposalVote2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(proposalVote);
          expect(expectedResult).toContain(proposalVote2);
        });

        it('should accept null and undefined values', () => {
          const proposalVote: IProposalVote = { id: 123 };
          expectedResult = service.addProposalVoteToCollectionIfMissing([], null, proposalVote, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(proposalVote);
        });

        it('should return initial array if no ProposalVote is added', () => {
          const proposalVoteCollection: IProposalVote[] = [{ id: 123 }];
          expectedResult = service.addProposalVoteToCollectionIfMissing(proposalVoteCollection, undefined, null);
          expect(expectedResult).toEqual(proposalVoteCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
