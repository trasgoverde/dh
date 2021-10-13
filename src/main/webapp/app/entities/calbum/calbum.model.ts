import * as dayjs from 'dayjs';
import { IPhoto } from 'app/entities/photo/photo.model';
import { ICommunity } from 'app/entities/community/community.model';

export interface ICalbum {
  id?: number;
  creationDate?: dayjs.Dayjs;
  title?: string;
  photos?: IPhoto[] | null;
  community?: ICommunity;
}

export class Calbum implements ICalbum {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public title?: string,
    public photos?: IPhoto[] | null,
    public community?: ICommunity
  ) {}
}

export function getCalbumIdentifier(calbum: ICalbum): number | undefined {
  return calbum.id;
}
