jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INewsletter, Newsletter } from '../newsletter.model';
import { NewsletterService } from '../service/newsletter.service';

import { NewsletterRoutingResolveService } from './newsletter-routing-resolve.service';

describe('Service Tests', () => {
  describe('Newsletter routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: NewsletterRoutingResolveService;
    let service: NewsletterService;
    let resultNewsletter: INewsletter | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(NewsletterRoutingResolveService);
      service = TestBed.inject(NewsletterService);
      resultNewsletter = undefined;
    });

    describe('resolve', () => {
      it('should return INewsletter returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNewsletter = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNewsletter).toEqual({ id: 123 });
      });

      it('should return new INewsletter if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNewsletter = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultNewsletter).toEqual(new Newsletter());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Newsletter })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNewsletter = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNewsletter).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
