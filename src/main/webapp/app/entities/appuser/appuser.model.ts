import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IAppprofile } from 'app/entities/appprofile/appprofile.model';
import { IAppphoto } from 'app/entities/appphoto/appphoto.model';
import { ICommunity } from 'app/entities/community/community.model';
import { IBlog } from 'app/entities/blog/blog.model';
import { INotification } from 'app/entities/notification/notification.model';
import { IAlbum } from 'app/entities/album/album.model';
import { IComment } from 'app/entities/comment/comment.model';
import { IPost } from 'app/entities/post/post.model';
import { IMessage } from 'app/entities/message/message.model';
import { IFollow } from 'app/entities/follow/follow.model';
import { IBlockuser } from 'app/entities/blockuser/blockuser.model';
import { IVtopic } from 'app/entities/vtopic/vtopic.model';
import { IVquestion } from 'app/entities/vquestion/vquestion.model';
import { IVanswer } from 'app/entities/vanswer/vanswer.model';
import { IVthumb } from 'app/entities/vthumb/vthumb.model';
import { IProposal } from 'app/entities/proposal/proposal.model';
import { IProposalVote } from 'app/entities/proposal-vote/proposal-vote.model';
import { IInterest } from 'app/entities/interest/interest.model';
import { IActivity } from 'app/entities/activity/activity.model';
import { ICeleb } from 'app/entities/celeb/celeb.model';

export interface IAppuser {
  id?: number;
  creationDate?: dayjs.Dayjs;
  assignedVotesPoints?: number | null;
  user?: IUser;
  appprofile?: IAppprofile | null;
  appphoto?: IAppphoto | null;
  communities?: ICommunity[] | null;
  blogs?: IBlog[] | null;
  notifications?: INotification[] | null;
  albums?: IAlbum[] | null;
  comments?: IComment[] | null;
  posts?: IPost[] | null;
  senders?: IMessage[] | null;
  receivers?: IMessage[] | null;
  followeds?: IFollow[] | null;
  followings?: IFollow[] | null;
  blockedusers?: IBlockuser[] | null;
  blockingusers?: IBlockuser[] | null;
  vtopics?: IVtopic[] | null;
  vquestions?: IVquestion[] | null;
  vanswers?: IVanswer[] | null;
  vthumbs?: IVthumb[] | null;
  proposals?: IProposal[] | null;
  proposalVotes?: IProposalVote[] | null;
  interests?: IInterest[] | null;
  activities?: IActivity[] | null;
  celebs?: ICeleb[] | null;
}

export class Appuser implements IAppuser {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public assignedVotesPoints?: number | null,
    public user?: IUser,
    public appprofile?: IAppprofile | null,
    public appphoto?: IAppphoto | null,
    public communities?: ICommunity[] | null,
    public blogs?: IBlog[] | null,
    public notifications?: INotification[] | null,
    public albums?: IAlbum[] | null,
    public comments?: IComment[] | null,
    public posts?: IPost[] | null,
    public senders?: IMessage[] | null,
    public receivers?: IMessage[] | null,
    public followeds?: IFollow[] | null,
    public followings?: IFollow[] | null,
    public blockedusers?: IBlockuser[] | null,
    public blockingusers?: IBlockuser[] | null,
    public vtopics?: IVtopic[] | null,
    public vquestions?: IVquestion[] | null,
    public vanswers?: IVanswer[] | null,
    public vthumbs?: IVthumb[] | null,
    public proposals?: IProposal[] | null,
    public proposalVotes?: IProposalVote[] | null,
    public interests?: IInterest[] | null,
    public activities?: IActivity[] | null,
    public celebs?: ICeleb[] | null
  ) {}
}

export function getAppuserIdentifier(appuser: IAppuser): number | undefined {
  return appuser.id;
}
