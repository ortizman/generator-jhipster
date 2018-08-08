/**
 * @license
 * Copyright Akveo. All Rights Reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { NbTokenService } from '@nebular/auth/services/token/token.service';

@Component({
  selector: 'nb-logout',
  template: `
    <div>Logging out, please wait...</div>
  `,
})
export class LogoutComponent implements OnInit {

  constructor(private _tokenService: NbTokenService,
              private _router: Router) {}

  ngOnInit(): void {
    this.logout();
  }

  logout(): void {
    this._tokenService.clear().subscribe(() => {
      this._router.navigate(['/auth/login']);
    });
  }

}
