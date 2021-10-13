import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVquestion } from '../vquestion.model';

@Component({
  selector: 'jhi-vquestion-detail',
  templateUrl: './vquestion-detail.component.html',
})
export class VquestionDetailComponent implements OnInit {
  vquestion: IVquestion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vquestion }) => {
      this.vquestion = vquestion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
