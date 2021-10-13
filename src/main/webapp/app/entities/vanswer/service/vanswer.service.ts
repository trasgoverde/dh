import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IVanswer, getVanswerIdentifier } from '../vanswer.model';

export type EntityResponseType = HttpResponse<IVanswer>;
export type EntityArrayResponseType = HttpResponse<IVanswer[]>;

@Injectable({ providedIn: 'root' })
export class VanswerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vanswers');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/vanswers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vanswer: IVanswer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vanswer);
    return this.http
      .post<IVanswer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vanswer: IVanswer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vanswer);
    return this.http
      .put<IVanswer>(`${this.resourceUrl}/${getVanswerIdentifier(vanswer) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(vanswer: IVanswer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vanswer);
    return this.http
      .patch<IVanswer>(`${this.resourceUrl}/${getVanswerIdentifier(vanswer) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVanswer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVanswer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVanswer[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addVanswerToCollectionIfMissing(vanswerCollection: IVanswer[], ...vanswersToCheck: (IVanswer | null | undefined)[]): IVanswer[] {
    const vanswers: IVanswer[] = vanswersToCheck.filter(isPresent);
    if (vanswers.length > 0) {
      const vanswerCollectionIdentifiers = vanswerCollection.map(vanswerItem => getVanswerIdentifier(vanswerItem)!);
      const vanswersToAdd = vanswers.filter(vanswerItem => {
        const vanswerIdentifier = getVanswerIdentifier(vanswerItem);
        if (vanswerIdentifier == null || vanswerCollectionIdentifiers.includes(vanswerIdentifier)) {
          return false;
        }
        vanswerCollectionIdentifiers.push(vanswerIdentifier);
        return true;
      });
      return [...vanswersToAdd, ...vanswerCollection];
    }
    return vanswerCollection;
  }

  protected convertDateFromClient(vanswer: IVanswer): IVanswer {
    return Object.assign({}, vanswer, {
      creationDate: vanswer.creationDate?.isValid() ? vanswer.creationDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate ? dayjs(res.body.creationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((vanswer: IVanswer) => {
        vanswer.creationDate = vanswer.creationDate ? dayjs(vanswer.creationDate) : undefined;
      });
    }
    return res;
  }
}
