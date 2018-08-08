import { NgModule } from '@angular/core';

import { ThemeModule } from '../@theme/theme.module';

import { AuthRoutingModule, routedComponents } from './auth-routing.module';

@NgModule({
  imports: [
    ThemeModule,
    AuthRoutingModule,
  ],
  declarations: [
    ...routedComponents,
  ],
  providers: [],
})
export class AuthModule { }
