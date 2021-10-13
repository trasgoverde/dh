import * as dayjs from 'dayjs';
import { IVthumb } from 'app/entities/vthumb/vthumb.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { IVquestion } from 'app/entities/vquestion/vquestion.model';

export interface IVanswer {
  id?: number;
  creationDate?: dayjs.Dayjs;
  urlVanswer?: string;
  accepted?: boolean | null;
  vthumbs?: IVthumb[] | null;
  appuser?: IAppuser;
  vquestion?: IVquestion;
}

export class Vanswer implements IVanswer {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public urlVanswer?: string,
    public accepted?: boolean | null,
    public vthumbs?: IVthumb[] | null,
    public appuser?: IAppuser,
    public vquestion?: IVquestion
  ) {
    this.accepted = this.accepted ?? false;
  }
}

export function getVanswerIdentifier(vanswer: IVanswer): number | undefined {
  return vanswer.id;
}
