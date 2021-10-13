import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INewsletter, Newsletter } from '../newsletter.model';
import { NewsletterService } from '../service/newsletter.service';

@Injectable({ providedIn: 'root' })
export class NewsletterRoutingResolveService implements Resolve<INewsletter> {
  constructor(protected service: NewsletterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INewsletter> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((newsletter: HttpResponse<Newsletter>) => {
          if (newsletter.body) {
            return of(newsletter.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Newsletter());
  }
}
