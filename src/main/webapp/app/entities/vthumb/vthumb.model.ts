import * as dayjs from 'dayjs';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { IVquestion } from 'app/entities/vquestion/vquestion.model';
import { IVanswer } from 'app/entities/vanswer/vanswer.model';

export interface IVthumb {
  id?: number;
  creationDate?: dayjs.Dayjs;
  vthumbUp?: boolean | null;
  vthumbDown?: boolean | null;
  appuser?: IAppuser;
  vquestion?: IVquestion | null;
  vanswer?: IVanswer | null;
}

export class Vthumb implements IVthumb {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public vthumbUp?: boolean | null,
    public vthumbDown?: boolean | null,
    public appuser?: IAppuser,
    public vquestion?: IVquestion | null,
    public vanswer?: IVanswer | null
  ) {
    this.vthumbUp = this.vthumbUp ?? false;
    this.vthumbDown = this.vthumbDown ?? false;
  }
}

export function getVthumbIdentifier(vthumb: IVthumb): number | undefined {
  return vthumb.id;
}
