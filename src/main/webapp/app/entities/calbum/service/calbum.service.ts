import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICalbum, getCalbumIdentifier } from '../calbum.model';

export type EntityResponseType = HttpResponse<ICalbum>;
export type EntityArrayResponseType = HttpResponse<ICalbum[]>;

@Injectable({ providedIn: 'root' })
export class CalbumService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/calbums');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/calbums');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(calbum: ICalbum): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calbum);
    return this.http
      .post<ICalbum>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(calbum: ICalbum): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calbum);
    return this.http
      .put<ICalbum>(`${this.resourceUrl}/${getCalbumIdentifier(calbum) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(calbum: ICalbum): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calbum);
    return this.http
      .patch<ICalbum>(`${this.resourceUrl}/${getCalbumIdentifier(calbum) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICalbum>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICalbum[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICalbum[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addCalbumToCollectionIfMissing(calbumCollection: ICalbum[], ...calbumsToCheck: (ICalbum | null | undefined)[]): ICalbum[] {
    const calbums: ICalbum[] = calbumsToCheck.filter(isPresent);
    if (calbums.length > 0) {
      const calbumCollectionIdentifiers = calbumCollection.map(calbumItem => getCalbumIdentifier(calbumItem)!);
      const calbumsToAdd = calbums.filter(calbumItem => {
        const calbumIdentifier = getCalbumIdentifier(calbumItem);
        if (calbumIdentifier == null || calbumCollectionIdentifiers.includes(calbumIdentifier)) {
          return false;
        }
        calbumCollectionIdentifiers.push(calbumIdentifier);
        return true;
      });
      return [...calbumsToAdd, ...calbumCollection];
    }
    return calbumCollection;
  }

  protected convertDateFromClient(calbum: ICalbum): ICalbum {
    return Object.assign({}, calbum, {
      creationDate: calbum.creationDate?.isValid() ? calbum.creationDate.toJSON() : undefined,
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
      res.body.forEach((calbum: ICalbum) => {
        calbum.creationDate = calbum.creationDate ? dayjs(calbum.creationDate) : undefined;
      });
    }
    return res;
  }
}
