import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVquestion, Vquestion } from '../vquestion.model';
import { VquestionService } from '../service/vquestion.service';

@Injectable({ providedIn: 'root' })
export class VquestionRoutingResolveService implements Resolve<IVquestion> {
  constructor(protected service: VquestionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVquestion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vquestion: HttpResponse<Vquestion>) => {
          if (vquestion.body) {
            return of(vquestion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vquestion());
  }
}
