import * as dayjs from 'dayjs';

export interface INewsletter {
  id?: number;
  creationDate?: dayjs.Dayjs;
  email?: string;
}

export class Newsletter implements INewsletter {
  constructor(public id?: number, public creationDate?: dayjs.Dayjs, public email?: string) {}
}

export function getNewsletterIdentifier(newsletter: INewsletter): number | undefined {
  return newsletter.id;
}
