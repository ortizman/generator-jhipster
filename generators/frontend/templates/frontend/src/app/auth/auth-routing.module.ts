import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AuthBlockComponent, LoginComponent, LogoutComponent } from './components';
import { AuthComponent } from './auth.component';
import { AuthGuard } from './providers/auth-guard.service';
import { RoleGuard } from './providers/role-guard.service';


const routes: Routes = [
  {
    path: '',
    component: AuthComponent,
    children: [
      {
        path: '',
        component: LoginComponent,
      },
      {
        path: 'login',
        component: LoginComponent,
      },
      {
        path: 'logout',
        component: LogoutComponent,
      },
    ],
  },
];

@NgModule({
  imports: [
    RouterModule.forChild(routes),
  ],
  exports: [
    RouterModule,
  ],
  providers: [
    AuthGuard,
    RoleGuard,
  ],
})
export class AuthRoutingModule {

}

export const routedComponents = [
  AuthComponent,
  AuthBlockComponent,
  LoginComponent,
  LogoutComponent,
];
