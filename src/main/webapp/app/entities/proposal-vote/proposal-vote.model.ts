import * as dayjs from 'dayjs';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { IProposal } from 'app/entities/proposal/proposal.model';

export interface IProposalVote {
  id?: number;
  creationDate?: dayjs.Dayjs;
  votePoints?: number;
  appuser?: IAppuser | null;
  proposal?: IProposal | null;
}

export class ProposalVote implements IProposalVote {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public votePoints?: number,
    public appuser?: IAppuser | null,
    public proposal?: IProposal | null
  ) {}
}

export function getProposalVoteIdentifier(proposalVote: IProposalVote): number | undefined {
  return proposalVote.id;
}
