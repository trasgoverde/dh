import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IVquestion, getVquestionIdentifier } from '../vquestion.model';

export type EntityResponseType = HttpResponse<IVquestion>;
export type EntityArrayResponseType = HttpResponse<IVquestion[]>;

@Injectable({ providedIn: 'root' })
export class VquestionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vquestions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/vquestions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vquestion: IVquestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vquestion);
    return this.http
      .post<IVquestion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vquestion: IVquestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vquestion);
    return this.http
      .put<IVquestion>(`${this.resourceUrl}/${getVquestionIdentifier(vquestion) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(vquestion: IVquestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vquestion);
    return this.http
      .patch<IVquestion>(`${this.resourceUrl}/${getVquestionIdentifier(vquestion) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVquestion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVquestion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVquestion[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addVquestionToCollectionIfMissing(
    vquestionCollection: IVquestion[],
    ...vquestionsToCheck: (IVquestion | null | undefined)[]
  ): IVquestion[] {
    const vquestions: IVquestion[] = vquestionsToCheck.filter(isPresent);
    if (vquestions.length > 0) {
      const vquestionCollectionIdentifiers = vquestionCollection.map(vquestionItem => getVquestionIdentifier(vquestionItem)!);
      const vquestionsToAdd = vquestions.filter(vquestionItem => {
        const vquestionIdentifier = getVquestionIdentifier(vquestionItem);
        if (vquestionIdentifier == null || vquestionCollectionIdentifiers.includes(vquestionIdentifier)) {
          return false;
        }
        vquestionCollectionIdentifiers.push(vquestionIdentifier);
        return true;
      });
      return [...vquestionsToAdd, ...vquestionCollection];
    }
    return vquestionCollection;
  }

  protected convertDateFromClient(vquestion: IVquestion): IVquestion {
    return Object.assign({}, vquestion, {
      creationDate: vquestion.creationDate?.isValid() ? vquestion.creationDate.toJSON() : undefined,
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
      res.body.forEach((vquestion: IVquestion) => {
        vquestion.creationDate = vquestion.creationDate ? dayjs(vquestion.creationDate) : undefined;
      });
    }
    return res;
  }
}
