import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IProposalVote, getProposalVoteIdentifier } from '../proposal-vote.model';

export type EntityResponseType = HttpResponse<IProposalVote>;
export type EntityArrayResponseType = HttpResponse<IProposalVote[]>;

@Injectable({ providedIn: 'root' })
export class ProposalVoteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/proposal-votes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/proposal-votes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(proposalVote: IProposalVote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proposalVote);
    return this.http
      .post<IProposalVote>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(proposalVote: IProposalVote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proposalVote);
    return this.http
      .put<IProposalVote>(`${this.resourceUrl}/${getProposalVoteIdentifier(proposalVote) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(proposalVote: IProposalVote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proposalVote);
    return this.http
      .patch<IProposalVote>(`${this.resourceUrl}/${getProposalVoteIdentifier(proposalVote) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProposalVote>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProposalVote[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProposalVote[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addProposalVoteToCollectionIfMissing(
    proposalVoteCollection: IProposalVote[],
    ...proposalVotesToCheck: (IProposalVote | null | undefined)[]
  ): IProposalVote[] {
    const proposalVotes: IProposalVote[] = proposalVotesToCheck.filter(isPresent);
    if (proposalVotes.length > 0) {
      const proposalVoteCollectionIdentifiers = proposalVoteCollection.map(
        proposalVoteItem => getProposalVoteIdentifier(proposalVoteItem)!
      );
      const proposalVotesToAdd = proposalVotes.filter(proposalVoteItem => {
        const proposalVoteIdentifier = getProposalVoteIdentifier(proposalVoteItem);
        if (proposalVoteIdentifier == null || proposalVoteCollectionIdentifiers.includes(proposalVoteIdentifier)) {
          return false;
        }
        proposalVoteCollectionIdentifiers.push(proposalVoteIdentifier);
        return true;
      });
      return [...proposalVotesToAdd, ...proposalVoteCollection];
    }
    return proposalVoteCollection;
  }

  protected convertDateFromClient(proposalVote: IProposalVote): IProposalVote {
    return Object.assign({}, proposalVote, {
      creationDate: proposalVote.creationDate?.isValid() ? proposalVote.creationDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate ? dayjs(res.body.creationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((proposalVote: IProposalVote) => {
        proposalVote.creationDate = proposalVote.creationDate ? dayjs(proposalVote.creationDate) : undefined;
      });
    }
    return res;
  }
}
