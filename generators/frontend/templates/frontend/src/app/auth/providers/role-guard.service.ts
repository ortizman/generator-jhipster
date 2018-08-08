import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot } from '@angular/router';
import { NbAuthService } from '@nebular/auth';
import { RoleService } from '../../@core/providers/role.service';


@Injectable()
export class RoleGuard implements CanActivate {

  constructor(private authService: NbAuthService, private roleService: RoleService) {
  }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRole: string [] = route.data.expectedRole;
    let result = false;
    this.authService.isAuthenticated().subscribe(authenticated => {
      result = (authenticated && (expectedRole.includes(this.roleService.getUserRole())));
    })
    return result;
  }
}
