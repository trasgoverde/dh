import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICmessage, getCmessageIdentifier } from '../cmessage.model';

export type EntityResponseType = HttpResponse<ICmessage>;
export type EntityArrayResponseType = HttpResponse<ICmessage[]>;

@Injectable({ providedIn: 'root' })
export class CmessageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cmessages');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/cmessages');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cmessage: ICmessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cmessage);
    return this.http
      .post<ICmessage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cmessage: ICmessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cmessage);
    return this.http
      .put<ICmessage>(`${this.resourceUrl}/${getCmessageIdentifier(cmessage) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(cmessage: ICmessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cmessage);
    return this.http
      .patch<ICmessage>(`${this.resourceUrl}/${getCmessageIdentifier(cmessage) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICmessage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICmessage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICmessage[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addCmessageToCollectionIfMissing(cmessageCollection: ICmessage[], ...cmessagesToCheck: (ICmessage | null | undefined)[]): ICmessage[] {
    const cmessages: ICmessage[] = cmessagesToCheck.filter(isPresent);
    if (cmessages.length > 0) {
      const cmessageCollectionIdentifiers = cmessageCollection.map(cmessageItem => getCmessageIdentifier(cmessageItem)!);
      const cmessagesToAdd = cmessages.filter(cmessageItem => {
        const cmessageIdentifier = getCmessageIdentifier(cmessageItem);
        if (cmessageIdentifier == null || cmessageCollectionIdentifiers.includes(cmessageIdentifier)) {
          return false;
        }
        cmessageCollectionIdentifiers.push(cmessageIdentifier);
        return true;
      });
      return [...cmessagesToAdd, ...cmessageCollection];
    }
    return cmessageCollection;
  }

  protected convertDateFromClient(cmessage: ICmessage): ICmessage {
    return Object.assign({}, cmessage, {
      creationDate: cmessage.creationDate?.isValid() ? cmessage.creationDate.toJSON() : undefined,
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
      res.body.forEach((cmessage: ICmessage) => {
        cmessage.creationDate = cmessage.creationDate ? dayjs(cmessage.creationDate) : undefined;
      });
    }
    return res;
  }
}
