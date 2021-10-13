import * as dayjs from 'dayjs';
import { IAlbum } from 'app/entities/album/album.model';
import { ICalbum } from 'app/entities/calbum/calbum.model';

export interface IPhoto {
  id?: number;
  creationDate?: dayjs.Dayjs;
  imageContentType?: string | null;
  image?: string | null;
  album?: IAlbum | null;
  calbum?: ICalbum | null;
}

export class Photo implements IPhoto {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public imageContentType?: string | null,
    public image?: string | null,
    public album?: IAlbum | null,
    public calbum?: ICalbum | null
  ) {}
}

export function getPhotoIdentifier(photo: IPhoto): number | undefined {
  return photo.id;
}
