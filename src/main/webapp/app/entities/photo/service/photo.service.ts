import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IPhoto, getPhotoIdentifier } from '../photo.model';

export type EntityResponseType = HttpResponse<IPhoto>;
export type EntityArrayResponseType = HttpResponse<IPhoto[]>;

@Injectable({ providedIn: 'root' })
export class PhotoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/photos');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/photos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(photo: IPhoto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(photo);
    return this.http
      .post<IPhoto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(photo: IPhoto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(photo);
    return this.http
      .put<IPhoto>(`${this.resourceUrl}/${getPhotoIdentifier(photo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(photo: IPhoto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(photo);
    return this.http
      .patch<IPhoto>(`${this.resourceUrl}/${getPhotoIdentifier(photo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPhoto>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPhoto[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPhoto[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addPhotoToCollectionIfMissing(photoCollection: IPhoto[], ...photosToCheck: (IPhoto | null | undefined)[]): IPhoto[] {
    const photos: IPhoto[] = photosToCheck.filter(isPresent);
    if (photos.length > 0) {
      const photoCollectionIdentifiers = photoCollection.map(photoItem => getPhotoIdentifier(photoItem)!);
      const photosToAdd = photos.filter(photoItem => {
        const photoIdentifier = getPhotoIdentifier(photoItem);
        if (photoIdentifier == null || photoCollectionIdentifiers.includes(photoIdentifier)) {
          return false;
        }
        photoCollectionIdentifiers.push(photoIdentifier);
        return true;
      });
      return [...photosToAdd, ...photoCollection];
    }
    return photoCollection;
  }

  protected convertDateFromClient(photo: IPhoto): IPhoto {
    return Object.assign({}, photo, {
      creationDate: photo.creationDate?.isValid() ? photo.creationDate.toJSON() : undefined,
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
      res.body.forEach((photo: IPhoto) => {
        photo.creationDate = photo.creationDate ? dayjs(photo.creationDate) : undefined;
      });
    }
    return res;
  }
}
