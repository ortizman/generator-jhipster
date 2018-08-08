import { Component } from '@angular/core';

@Component({
  selector: 'ngx-footer',
  styleUrls: ['./footer.component.scss'],
  template: `
    <div class="socials">
      <a href="https://www.facebook.com/fluxitsoft" target="_blank" class="ion ion-social-facebook"></a>
      <a href="https://twitter.com/fluxitsoft" target="_blank" class="ion ion-social-twitter"></a>
      <a href="https://www.linkedin.com/company/fluxit" target="_blank" class="ion ion-social-linkedin"></a>
    </div>
  `,
})
export class FooterComponent {
}
