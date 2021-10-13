import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IVtopic, getVtopicIdentifier } from '../vtopic.model';

export type EntityResponseType = HttpResponse<IVtopic>;
export type EntityArrayResponseType = HttpResponse<IVtopic[]>;

@Injectable({ providedIn: 'root' })
export class VtopicService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vtopics');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/vtopics');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vtopic: IVtopic): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vtopic);
    return this.http
      .post<IVtopic>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vtopic: IVtopic): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vtopic);
    return this.http
      .put<IVtopic>(`${this.resourceUrl}/${getVtopicIdentifier(vtopic) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(vtopic: IVtopic): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vtopic);
    return this.http
      .patch<IVtopic>(`${this.resourceUrl}/${getVtopicIdentifier(vtopic) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVtopic>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVtopic[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVtopic[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addVtopicToCollectionIfMissing(vtopicCollection: IVtopic[], ...vtopicsToCheck: (IVtopic | null | undefined)[]): IVtopic[] {
    const vtopics: IVtopic[] = vtopicsToCheck.filter(isPresent);
    if (vtopics.length > 0) {
      const vtopicCollectionIdentifiers = vtopicCollection.map(vtopicItem => getVtopicIdentifier(vtopicItem)!);
      const vtopicsToAdd = vtopics.filter(vtopicItem => {
        const vtopicIdentifier = getVtopicIdentifier(vtopicItem);
        if (vtopicIdentifier == null || vtopicCollectionIdentifiers.includes(vtopicIdentifier)) {
          return false;
        }
        vtopicCollectionIdentifiers.push(vtopicIdentifier);
        return true;
      });
      return [...vtopicsToAdd, ...vtopicCollection];
    }
    return vtopicCollection;
  }

  protected convertDateFromClient(vtopic: IVtopic): IVtopic {
    return Object.assign({}, vtopic, {
      creationDate: vtopic.creationDate?.isValid() ? vtopic.creationDate.toJSON() : undefined,
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
      res.body.forEach((vtopic: IVtopic) => {
        vtopic.creationDate = vtopic.creationDate ? dayjs(vtopic.creationDate) : undefined;
      });
    }
    return res;
  }
}
