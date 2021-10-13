import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVtopic } from '../vtopic.model';

@Component({
  selector: 'jhi-vtopic-detail',
  templateUrl: './vtopic-detail.component.html',
})
export class VtopicDetailComponent implements OnInit {
  vtopic: IVtopic | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vtopic }) => {
      this.vtopic = vtopic;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
