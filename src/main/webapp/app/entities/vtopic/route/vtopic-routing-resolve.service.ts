import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVtopic, Vtopic } from '../vtopic.model';
import { VtopicService } from '../service/vtopic.service';

@Injectable({ providedIn: 'root' })
export class VtopicRoutingResolveService implements Resolve<IVtopic> {
  constructor(protected service: VtopicService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVtopic> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vtopic: HttpResponse<Vtopic>) => {
          if (vtopic.body) {
            return of(vtopic.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vtopic());
  }
}
