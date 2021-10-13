import * as dayjs from 'dayjs';
import { IVanswer } from 'app/entities/vanswer/vanswer.model';
import { IVthumb } from 'app/entities/vthumb/vthumb.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { IVtopic } from 'app/entities/vtopic/vtopic.model';

export interface IVquestion {
  id?: number;
  creationDate?: dayjs.Dayjs;
  vquestion?: string;
  vquestionDescription?: string | null;
  vanswers?: IVanswer[] | null;
  vthumbs?: IVthumb[] | null;
  appuser?: IAppuser;
  vtopic?: IVtopic;
}

export class Vquestion implements IVquestion {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public vquestion?: string,
    public vquestionDescription?: string | null,
    public vanswers?: IVanswer[] | null,
    public vthumbs?: IVthumb[] | null,
    public appuser?: IAppuser,
    public vtopic?: IVtopic
  ) {}
}

export function getVquestionIdentifier(vquestion: IVquestion): number | undefined {
  return vquestion.id;
}
