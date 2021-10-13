import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVthumb, Vthumb } from '../vthumb.model';
import { VthumbService } from '../service/vthumb.service';

@Injectable({ providedIn: 'root' })
export class VthumbRoutingResolveService implements Resolve<IVthumb> {
  constructor(protected service: VthumbService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVthumb> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vthumb: HttpResponse<Vthumb>) => {
          if (vthumb.body) {
            return of(vthumb.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vthumb());
  }
}
