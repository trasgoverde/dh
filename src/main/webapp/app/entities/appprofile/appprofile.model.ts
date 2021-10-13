import * as dayjs from 'dayjs';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { CivilStatus } from 'app/entities/enumerations/civil-status.model';
import { Purpose } from 'app/entities/enumerations/purpose.model';
import { Physical } from 'app/entities/enumerations/physical.model';
import { Religion } from 'app/entities/enumerations/religion.model';
import { EthnicGroup } from 'app/entities/enumerations/ethnic-group.model';
import { Studies } from 'app/entities/enumerations/studies.model';
import { Eyes } from 'app/entities/enumerations/eyes.model';
import { Smoker } from 'app/entities/enumerations/smoker.model';
import { Children } from 'app/entities/enumerations/children.model';
import { FutureChildren } from 'app/entities/enumerations/future-children.model';

export interface IAppprofile {
  id?: number;
  creationDate?: dayjs.Dayjs;
  gender?: Gender | null;
  phone?: string | null;
  bio?: string | null;
  facebook?: string | null;
  twitter?: string | null;
  linkedin?: string | null;
  instagram?: string | null;
  googlePlus?: string | null;
  birthdate?: dayjs.Dayjs | null;
  civilStatus?: CivilStatus | null;
  lookingFor?: Gender | null;
  purpose?: Purpose | null;
  physical?: Physical | null;
  religion?: Religion | null;
  ethnicGroup?: EthnicGroup | null;
  studies?: Studies | null;
  sibblings?: number | null;
  eyes?: Eyes | null;
  smoker?: Smoker | null;
  children?: Children | null;
  futureChildren?: FutureChildren | null;
  pet?: boolean | null;
  appuser?: IAppuser;
}

export class Appprofile implements IAppprofile {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public gender?: Gender | null,
    public phone?: string | null,
    public bio?: string | null,
    public facebook?: string | null,
    public twitter?: string | null,
    public linkedin?: string | null,
    public instagram?: string | null,
    public googlePlus?: string | null,
    public birthdate?: dayjs.Dayjs | null,
    public civilStatus?: CivilStatus | null,
    public lookingFor?: Gender | null,
    public purpose?: Purpose | null,
    public physical?: Physical | null,
    public religion?: Religion | null,
    public ethnicGroup?: EthnicGroup | null,
    public studies?: Studies | null,
    public sibblings?: number | null,
    public eyes?: Eyes | null,
    public smoker?: Smoker | null,
    public children?: Children | null,
    public futureChildren?: FutureChildren | null,
    public pet?: boolean | null,
    public appuser?: IAppuser
  ) {
    this.pet = this.pet ?? false;
  }
}

export function getAppprofileIdentifier(appprofile: IAppprofile): number | undefined {
  return appprofile.id;
}
