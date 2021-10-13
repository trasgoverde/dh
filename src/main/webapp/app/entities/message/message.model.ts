import * as dayjs from 'dayjs';
import { IAppuser } from 'app/entities/appuser/appuser.model';

export interface IMessage {
  id?: number;
  creationDate?: dayjs.Dayjs;
  messageText?: string;
  isDelivered?: boolean | null;
  sender?: IAppuser | null;
  receiver?: IAppuser | null;
}

export class Message implements IMessage {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public messageText?: string,
    public isDelivered?: boolean | null,
    public sender?: IAppuser | null,
    public receiver?: IAppuser | null
  ) {
    this.isDelivered = this.isDelivered ?? false;
  }
}

export function getMessageIdentifier(message: IMessage): number | undefined {
  return message.id;
}
