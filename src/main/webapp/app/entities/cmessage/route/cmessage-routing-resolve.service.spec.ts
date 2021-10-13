jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICmessage, Cmessage } from '../cmessage.model';
import { CmessageService } from '../service/cmessage.service';

import { CmessageRoutingResolveService } from './cmessage-routing-resolve.service';

describe('Service Tests', () => {
  describe('Cmessage routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CmessageRoutingResolveService;
    let service: CmessageService;
    let resultCmessage: ICmessage | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CmessageRoutingResolveService);
      service = TestBed.inject(CmessageService);
      resultCmessage = undefined;
    });

    describe('resolve', () => {
      it('should return ICmessage returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCmessage = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCmessage).toEqual({ id: 123 });
      });

      it('should return new ICmessage if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCmessage = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCmessage).toEqual(new Cmessage());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Cmessage })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCmessage = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCmessage).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
