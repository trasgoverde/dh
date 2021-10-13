import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICalbum, Calbum } from '../calbum.model';
import { CalbumService } from '../service/calbum.service';

@Injectable({ providedIn: 'root' })
export class CalbumRoutingResolveService implements Resolve<ICalbum> {
  constructor(protected service: CalbumService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICalbum> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((calbum: HttpResponse<Calbum>) => {
          if (calbum.body) {
            return of(calbum.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Calbum());
  }
}
