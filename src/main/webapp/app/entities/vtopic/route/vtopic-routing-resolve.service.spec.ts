jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVtopic, Vtopic } from '../vtopic.model';
import { VtopicService } from '../service/vtopic.service';

import { VtopicRoutingResolveService } from './vtopic-routing-resolve.service';

describe('Service Tests', () => {
  describe('Vtopic routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: VtopicRoutingResolveService;
    let service: VtopicService;
    let resultVtopic: IVtopic | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(VtopicRoutingResolveService);
      service = TestBed.inject(VtopicService);
      resultVtopic = undefined;
    });

    describe('resolve', () => {
      it('should return IVtopic returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVtopic = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVtopic).toEqual({ id: 123 });
      });

      it('should return new IVtopic if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVtopic = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultVtopic).toEqual(new Vtopic());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Vtopic })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVtopic = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVtopic).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
