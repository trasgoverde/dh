import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAlbum, getAlbumIdentifier } from '../album.model';

export type EntityResponseType = HttpResponse<IAlbum>;
export type EntityArrayResponseType = HttpResponse<IAlbum[]>;

@Injectable({ providedIn: 'root' })
export class AlbumService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/albums');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/albums');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(album: IAlbum): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(album);
    return this.http
      .post<IAlbum>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(album: IAlbum): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(album);
    return this.http
      .put<IAlbum>(`${this.resourceUrl}/${getAlbumIdentifier(album) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(album: IAlbum): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(album);
    return this.http
      .patch<IAlbum>(`${this.resourceUrl}/${getAlbumIdentifier(album) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAlbum>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAlbum[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAlbum[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAlbumToCollectionIfMissing(albumCollection: IAlbum[], ...albumsToCheck: (IAlbum | null | undefined)[]): IAlbum[] {
    const albums: IAlbum[] = albumsToCheck.filter(isPresent);
    if (albums.length > 0) {
      const albumCollectionIdentifiers = albumCollection.map(albumItem => getAlbumIdentifier(albumItem)!);
      const albumsToAdd = albums.filter(albumItem => {
        const albumIdentifier = getAlbumIdentifier(albumItem);
        if (albumIdentifier == null || albumCollectionIdentifiers.includes(albumIdentifier)) {
          return false;
        }
        albumCollectionIdentifiers.push(albumIdentifier);
        return true;
      });
      return [...albumsToAdd, ...albumCollection];
    }
    return albumCollection;
  }

  protected convertDateFromClient(album: IAlbum): IAlbum {
    return Object.assign({}, album, {
      creationDate: album.creationDate?.isValid() ? album.creationDate.toJSON() : undefined,
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
      res.body.forEach((album: IAlbum) => {
        album.creationDate = album.creationDate ? dayjs(album.creationDate) : undefined;
      });
    }
    return res;
  }
}
