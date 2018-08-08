import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { map } from 'rxjs/operators/map';

import { NbAuthService, NbAuthJWTToken } from '@nebular/auth';
import { NbRoleProvider } from '@nebular/security';
import { NbMenuItem } from '@nebular/theme';

@Injectable()
export class RoleService implements NbRoleProvider {

  private user: any;
  private menu: NbMenuItem[] = [
    {
      title: 'Dashboard',
      icon: 'nb-home',
      link: '/pages/service/list',
      home: true,
    },
    {
      title: 'Contratos',
      icon: 'nb-compose',
      link: '/pages/contracts/list',
    },
    {
      title: 'Administración',
      icon: 'nb-gear',
      children: [
        {
          title: 'Empresas',
          link: '/pages/companies/list',
        },
        {
          title: 'Usuarios',
          link: '/pages/users/list',
        },
      ],
    },
  ];

  constructor(private authService: NbAuthService) {
  }

  getRole(): Observable<string> {
    return this.authService.onTokenChange()
      .pipe(
        map((token: NbAuthJWTToken) => {
          if (token.isValid()) {
            this.user = token.getPayload();
            if (this.user.authorities.includes('ROLE_GR-ISS-ADMIN')) {
              this.user.role = 'admin'
            } else if (this.user.authorities.includes('ROLE_GR-ISS-ADVISER')) {
              this.user.role = 'internal'
            } else if (this.user.authorities.includes('ROLE_EXTERNAL')) {
              this.user.role = 'external'
            }
            return this.user.role;
          }
          return null;
        }),
    );
  }

  getMenu(): Observable<NbMenuItem[]> {
    return this.getRole().pipe(map((rol: string) => {
      if (rol === 'admin') {
        this.menu[0].hidden = false;
        this.menu[1].hidden = false;
        this.menu[2].hidden = false;
        return this.menu
      } if (rol === 'internal') {
        this.menu[0].hidden = false;
        this.menu[1].hidden = false;
        this.menu[2].hidden = true;
        return this.menu;
      } if (rol === 'external') {
        this.menu[0].hidden = false;
        this.menu[1].hidden = true;
        this.menu[2].hidden = true;
        return this.menu;
      }
    }))
  }

  getUserRole(): string {
    return this.user.role;
  }

  getUserCompany(): number {
    return this.user.companyId;
  }
}
