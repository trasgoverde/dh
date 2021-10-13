import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAppprofile, Appprofile } from '../appprofile.model';
import { AppprofileService } from '../service/appprofile.service';

@Injectable({ providedIn: 'root' })
export class AppprofileRoutingResolveService implements Resolve<IAppprofile> {
  constructor(protected service: AppprofileService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAppprofile> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((appprofile: HttpResponse<Appprofile>) => {
          if (appprofile.body) {
            return of(appprofile.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Appprofile());
  }
}
