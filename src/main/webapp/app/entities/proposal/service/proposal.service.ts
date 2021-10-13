import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IProposal, getProposalIdentifier } from '../proposal.model';

export type EntityResponseType = HttpResponse<IProposal>;
export type EntityArrayResponseType = HttpResponse<IProposal[]>;

@Injectable({ providedIn: 'root' })
export class ProposalService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/proposals');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/proposals');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(proposal: IProposal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proposal);
    return this.http
      .post<IProposal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(proposal: IProposal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proposal);
    return this.http
      .put<IProposal>(`${this.resourceUrl}/${getProposalIdentifier(proposal) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(proposal: IProposal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proposal);
    return this.http
      .patch<IProposal>(`${this.resourceUrl}/${getProposalIdentifier(proposal) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProposal>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProposal[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProposal[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addProposalToCollectionIfMissing(proposalCollection: IProposal[], ...proposalsToCheck: (IProposal | null | undefined)[]): IProposal[] {
    const proposals: IProposal[] = proposalsToCheck.filter(isPresent);
    if (proposals.length > 0) {
      const proposalCollectionIdentifiers = proposalCollection.map(proposalItem => getProposalIdentifier(proposalItem)!);
      const proposalsToAdd = proposals.filter(proposalItem => {
        const proposalIdentifier = getProposalIdentifier(proposalItem);
        if (proposalIdentifier == null || proposalCollectionIdentifiers.includes(proposalIdentifier)) {
          return false;
        }
        proposalCollectionIdentifiers.push(proposalIdentifier);
        return true;
      });
      return [...proposalsToAdd, ...proposalCollection];
    }
    return proposalCollection;
  }

  protected convertDateFromClient(proposal: IProposal): IProposal {
    return Object.assign({}, proposal, {
      creationDate: proposal.creationDate?.isValid() ? proposal.creationDate.toJSON() : undefined,
      releaseDate: proposal.releaseDate?.isValid() ? proposal.releaseDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate ? dayjs(res.body.creationDate) : undefined;
      res.body.releaseDate = res.body.releaseDate ? dayjs(res.body.releaseDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((proposal: IProposal) => {
        proposal.creationDate = proposal.creationDate ? dayjs(proposal.creationDate) : undefined;
        proposal.releaseDate = proposal.releaseDate ? dayjs(proposal.releaseDate) : undefined;
      });
    }
    return res;
  }
}
