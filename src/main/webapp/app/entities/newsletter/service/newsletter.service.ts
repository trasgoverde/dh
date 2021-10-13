import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { INewsletter, getNewsletterIdentifier } from '../newsletter.model';

export type EntityResponseType = HttpResponse<INewsletter>;
export type EntityArrayResponseType = HttpResponse<INewsletter[]>;

@Injectable({ providedIn: 'root' })
export class NewsletterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/newsletters');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/newsletters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(newsletter: INewsletter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(newsletter);
    return this.http
      .post<INewsletter>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(newsletter: INewsletter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(newsletter);
    return this.http
      .put<INewsletter>(`${this.resourceUrl}/${getNewsletterIdentifier(newsletter) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(newsletter: INewsletter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(newsletter);
    return this.http
      .patch<INewsletter>(`${this.resourceUrl}/${getNewsletterIdentifier(newsletter) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<INewsletter>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INewsletter[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INewsletter[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addNewsletterToCollectionIfMissing(
    newsletterCollection: INewsletter[],
    ...newslettersToCheck: (INewsletter | null | undefined)[]
  ): INewsletter[] {
    const newsletters: INewsletter[] = newslettersToCheck.filter(isPresent);
    if (newsletters.length > 0) {
      const newsletterCollectionIdentifiers = newsletterCollection.map(newsletterItem => getNewsletterIdentifier(newsletterItem)!);
      const newslettersToAdd = newsletters.filter(newsletterItem => {
        const newsletterIdentifier = getNewsletterIdentifier(newsletterItem);
        if (newsletterIdentifier == null || newsletterCollectionIdentifiers.includes(newsletterIdentifier)) {
          return false;
        }
        newsletterCollectionIdentifiers.push(newsletterIdentifier);
        return true;
      });
      return [...newslettersToAdd, ...newsletterCollection];
    }
    return newsletterCollection;
  }

  protected convertDateFromClient(newsletter: INewsletter): INewsletter {
    return Object.assign({}, newsletter, {
      creationDate: newsletter.creationDate?.isValid() ? newsletter.creationDate.toJSON() : undefined,
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
      res.body.forEach((newsletter: INewsletter) => {
        newsletter.creationDate = newsletter.creationDate ? dayjs(newsletter.creationDate) : undefined;
      });
    }
    return res;
  }
}
