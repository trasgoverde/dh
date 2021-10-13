import * as dayjs from 'dayjs';
import { IPhoto } from 'app/entities/photo/photo.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';

export interface IAlbum {
  id?: number;
  creationDate?: dayjs.Dayjs;
  title?: string;
  photos?: IPhoto[] | null;
  appuser?: IAppuser;
}

export class Album implements IAlbum {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public title?: string,
    public photos?: IPhoto[] | null,
    public appuser?: IAppuser
  ) {}
}

export function getAlbumIdentifier(album: IAlbum): number | undefined {
  return album.id;
}
