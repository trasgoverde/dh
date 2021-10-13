import * as dayjs from 'dayjs';
import { IBlog } from 'app/entities/blog/blog.model';
import { ICmessage } from 'app/entities/cmessage/cmessage.model';
import { IFollow } from 'app/entities/follow/follow.model';
import { IBlockuser } from 'app/entities/blockuser/blockuser.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { ICalbum } from 'app/entities/calbum/calbum.model';
import { ICinterest } from 'app/entities/cinterest/cinterest.model';
import { ICactivity } from 'app/entities/cactivity/cactivity.model';
import { ICceleb } from 'app/entities/cceleb/cceleb.model';

export interface ICommunity {
  id?: number;
  creationDate?: dayjs.Dayjs;
  communityName?: string;
  communityDescription?: string;
  imageContentType?: string | null;
  image?: string | null;
  isActive?: boolean | null;
  blogs?: IBlog[] | null;
  csenders?: ICmessage[] | null;
  creceivers?: ICmessage[] | null;
  cfolloweds?: IFollow[] | null;
  cfollowings?: IFollow[] | null;
  cblockedusers?: IBlockuser[] | null;
  cblockingusers?: IBlockuser[] | null;
  appuser?: IAppuser;
  calbums?: ICalbum[];
  cinterests?: ICinterest[] | null;
  cactivities?: ICactivity[] | null;
  ccelebs?: ICceleb[] | null;
}

export class Community implements ICommunity {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public communityName?: string,
    public communityDescription?: string,
    public imageContentType?: string | null,
    public image?: string | null,
    public isActive?: boolean | null,
    public blogs?: IBlog[] | null,
    public csenders?: ICmessage[] | null,
    public creceivers?: ICmessage[] | null,
    public cfolloweds?: IFollow[] | null,
    public cfollowings?: IFollow[] | null,
    public cblockedusers?: IBlockuser[] | null,
    public cblockingusers?: IBlockuser[] | null,
    public appuser?: IAppuser,
    public calbums?: ICalbum[],
    public cinterests?: ICinterest[] | null,
    public cactivities?: ICactivity[] | null,
    public ccelebs?: ICceleb[] | null
  ) {
    this.isActive = this.isActive ?? false;
  }
}

export function getCommunityIdentifier(community: ICommunity): number | undefined {
  return community.id;
}
