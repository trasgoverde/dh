import * as dayjs from 'dayjs';
import { IProposalVote } from 'app/entities/proposal-vote/proposal-vote.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { IPost } from 'app/entities/post/post.model';
import { ProposalType } from 'app/entities/enumerations/proposal-type.model';
import { ProposalRole } from 'app/entities/enumerations/proposal-role.model';

export interface IProposal {
  id?: number;
  creationDate?: dayjs.Dayjs;
  proposalName?: string;
  proposalType?: ProposalType;
  proposalRole?: ProposalRole;
  releaseDate?: dayjs.Dayjs | null;
  isOpen?: boolean | null;
  isAccepted?: boolean | null;
  isPaid?: boolean | null;
  proposalVotes?: IProposalVote[] | null;
  appuser?: IAppuser | null;
  post?: IPost | null;
}

export class Proposal implements IProposal {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public proposalName?: string,
    public proposalType?: ProposalType,
    public proposalRole?: ProposalRole,
    public releaseDate?: dayjs.Dayjs | null,
    public isOpen?: boolean | null,
    public isAccepted?: boolean | null,
    public isPaid?: boolean | null,
    public proposalVotes?: IProposalVote[] | null,
    public appuser?: IAppuser | null,
    public post?: IPost | null
  ) {
    this.isOpen = this.isOpen ?? false;
    this.isAccepted = this.isAccepted ?? false;
    this.isPaid = this.isPaid ?? false;
  }
}

export function getProposalIdentifier(proposal: IProposal): number | undefined {
  return proposal.id;
}
