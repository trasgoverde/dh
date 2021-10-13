import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVanswer, Vanswer } from '../vanswer.model';
import { VanswerService } from '../service/vanswer.service';

@Injectable({ providedIn: 'root' })
export class VanswerRoutingResolveService implements Resolve<IVanswer> {
  constructor(protected service: VanswerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVanswer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vanswer: HttpResponse<Vanswer>) => {
          if (vanswer.body) {
            return of(vanswer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vanswer());
  }
}
