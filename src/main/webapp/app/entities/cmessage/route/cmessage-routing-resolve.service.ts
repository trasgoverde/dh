import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICmessage, Cmessage } from '../cmessage.model';
import { CmessageService } from '../service/cmessage.service';

@Injectable({ providedIn: 'root' })
export class CmessageRoutingResolveService implements Resolve<ICmessage> {
  constructor(protected service: CmessageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICmessage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cmessage: HttpResponse<Cmessage>) => {
          if (cmessage.body) {
            return of(cmessage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cmessage());
  }
}
