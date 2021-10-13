import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAppprofile, getAppprofileIdentifier } from '../appprofile.model';

export type EntityResponseType = HttpResponse<IAppprofile>;
export type EntityArrayResponseType = HttpResponse<IAppprofile[]>;

@Injectable({ providedIn: 'root' })
export class AppprofileService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/appprofiles');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/appprofiles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(appprofile: IAppprofile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appprofile);
    return this.http
      .post<IAppprofile>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(appprofile: IAppprofile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appprofile);
    return this.http
      .put<IAppprofile>(`${this.resourceUrl}/${getAppprofileIdentifier(appprofile) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(appprofile: IAppprofile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appprofile);
    return this.http
      .patch<IAppprofile>(`${this.resourceUrl}/${getAppprofileIdentifier(appprofile) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAppprofile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAppprofile[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAppprofile[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAppprofileToCollectionIfMissing(
    appprofileCollection: IAppprofile[],
    ...appprofilesToCheck: (IAppprofile | null | undefined)[]
  ): IAppprofile[] {
    const appprofiles: IAppprofile[] = appprofilesToCheck.filter(isPresent);
    if (appprofiles.length > 0) {
      const appprofileCollectionIdentifiers = appprofileCollection.map(appprofileItem => getAppprofileIdentifier(appprofileItem)!);
      const appprofilesToAdd = appprofiles.filter(appprofileItem => {
        const appprofileIdentifier = getAppprofileIdentifier(appprofileItem);
        if (appprofileIdentifier == null || appprofileCollectionIdentifiers.includes(appprofileIdentifier)) {
          return false;
        }
        appprofileCollectionIdentifiers.push(appprofileIdentifier);
        return true;
      });
      return [...appprofilesToAdd, ...appprofileCollection];
    }
    return appprofileCollection;
  }

  protected convertDateFromClient(appprofile: IAppprofile): IAppprofile {
    return Object.assign({}, appprofile, {
      creationDate: appprofile.creationDate?.isValid() ? appprofile.creationDate.toJSON() : undefined,
      birthdate: appprofile.birthdate?.isValid() ? appprofile.birthdate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate ? dayjs(res.body.creationDate) : undefined;
      res.body.birthdate = res.body.birthdate ? dayjs(res.body.birthdate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((appprofile: IAppprofile) => {
        appprofile.creationDate = appprofile.creationDate ? dayjs(appprofile.creationDate) : undefined;
        appprofile.birthdate = appprofile.birthdate ? dayjs(appprofile.birthdate) : undefined;
      });
    }
    return res;
  }
}
