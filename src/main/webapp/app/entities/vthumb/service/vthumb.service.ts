import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IVthumb, getVthumbIdentifier } from '../vthumb.model';

export type EntityResponseType = HttpResponse<IVthumb>;
export type EntityArrayResponseType = HttpResponse<IVthumb[]>;

@Injectable({ providedIn: 'root' })
export class VthumbService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vthumbs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/vthumbs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vthumb: IVthumb): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vthumb);
    return this.http
      .post<IVthumb>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vthumb: IVthumb): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vthumb);
    return this.http
      .put<IVthumb>(`${this.resourceUrl}/${getVthumbIdentifier(vthumb) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(vthumb: IVthumb): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vthumb);
    return this.http
      .patch<IVthumb>(`${this.resourceUrl}/${getVthumbIdentifier(vthumb) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVthumb>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVthumb[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVthumb[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addVthumbToCollectionIfMissing(vthumbCollection: IVthumb[], ...vthumbsToCheck: (IVthumb | null | undefined)[]): IVthumb[] {
    const vthumbs: IVthumb[] = vthumbsToCheck.filter(isPresent);
    if (vthumbs.length > 0) {
      const vthumbCollectionIdentifiers = vthumbCollection.map(vthumbItem => getVthumbIdentifier(vthumbItem)!);
      const vthumbsToAdd = vthumbs.filter(vthumbItem => {
        const vthumbIdentifier = getVthumbIdentifier(vthumbItem);
        if (vthumbIdentifier == null || vthumbCollectionIdentifiers.includes(vthumbIdentifier)) {
          return false;
        }
        vthumbCollectionIdentifiers.push(vthumbIdentifier);
        return true;
      });
      return [...vthumbsToAdd, ...vthumbCollection];
    }
    return vthumbCollection;
  }

  protected convertDateFromClient(vthumb: IVthumb): IVthumb {
    return Object.assign({}, vthumb, {
      creationDate: vthumb.creationDate?.isValid() ? vthumb.creationDate.toJSON() : undefined,
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
      res.body.forEach((vthumb: IVthumb) => {
        vthumb.creationDate = vthumb.creationDate ? dayjs(vthumb.creationDate) : undefined;
      });
    }
    return res;
  }
}
