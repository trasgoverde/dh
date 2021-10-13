import * as dayjs from 'dayjs';
import { ICommunity } from 'app/entities/community/community.model';

export interface ICmessage {
  id?: number;
  creationDate?: dayjs.Dayjs;
  messageText?: string;
  isDelivered?: boolean | null;
  csender?: ICommunity | null;
  creceiver?: ICommunity | null;
}

export class Cmessage implements ICmessage {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public messageText?: string,
    public isDelivered?: boolean | null,
    public csender?: ICommunity | null,
    public creceiver?: ICommunity | null
  ) {
    this.isDelivered = this.isDelivered ?? false;
  }
}

export function getCmessageIdentifier(cmessage: ICmessage): number | undefined {
  return cmessage.id;
}
