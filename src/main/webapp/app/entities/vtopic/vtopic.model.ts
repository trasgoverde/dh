import * as dayjs from 'dayjs';
import { IVquestion } from 'app/entities/vquestion/vquestion.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';

export interface IVtopic {
  id?: number;
  creationDate?: dayjs.Dayjs;
  vtopicTitle?: string;
  vtopicDescription?: string | null;
  vquestions?: IVquestion[] | null;
  appuser?: IAppuser;
}

export class Vtopic implements IVtopic {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public vtopicTitle?: string,
    public vtopicDescription?: string | null,
    public vquestions?: IVquestion[] | null,
    public appuser?: IAppuser
  ) {}
}

export function getVtopicIdentifier(vtopic: IVtopic): number | undefined {
  return vtopic.id;
}
