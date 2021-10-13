jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVanswer, Vanswer } from '../vanswer.model';
import { VanswerService } from '../service/vanswer.service';

import { VanswerRoutingResolveService } from './vanswer-routing-resolve.service';

describe('Service Tests', () => {
  describe('Vanswer routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: VanswerRoutingResolveService;
    let service: VanswerService;
    let resultVanswer: IVanswer | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(VanswerRoutingResolveService);
      service = TestBed.inject(VanswerService);
      resultVanswer = undefined;
    });

    describe('resolve', () => {
      it('should return IVanswer returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVanswer = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVanswer).toEqual({ id: 123 });
      });

      it('should return new IVanswer if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVanswer = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultVanswer).toEqual(new Vanswer());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Vanswer })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVanswer = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVanswer).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
