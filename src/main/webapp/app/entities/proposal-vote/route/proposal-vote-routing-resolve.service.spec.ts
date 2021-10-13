jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IProposalVote, ProposalVote } from '../proposal-vote.model';
import { ProposalVoteService } from '../service/proposal-vote.service';

import { ProposalVoteRoutingResolveService } from './proposal-vote-routing-resolve.service';

describe('Service Tests', () => {
  describe('ProposalVote routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ProposalVoteRoutingResolveService;
    let service: ProposalVoteService;
    let resultProposalVote: IProposalVote | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ProposalVoteRoutingResolveService);
      service = TestBed.inject(ProposalVoteService);
      resultProposalVote = undefined;
    });

    describe('resolve', () => {
      it('should return IProposalVote returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProposalVote = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultProposalVote).toEqual({ id: 123 });
      });

      it('should return new IProposalVote if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProposalVote = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultProposalVote).toEqual(new ProposalVote());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ProposalVote })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProposalVote = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultProposalVote).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
