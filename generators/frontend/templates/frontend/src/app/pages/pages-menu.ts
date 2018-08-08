import { NbMenuItem } from '@nebular/theme';

export const MENU_ITEMS: NbMenuItem[] = [
  {
    title: 'Dashboard',
    icon: 'nb-home',
    link: '/pages/dashboard',
    home: true,
  },
  {
    title:'Administración',
    group: true,
  },
  {
    title: 'ABM de Entidades',
    icon: 'nb-tables',
    children: [
      {
        title: 'Usuarios',
        link: '/pages/users/list',
      },
    ],
  },
  {
    title: 'API docs',
    icon: 'nb-compose',
    url: 'http://petstore.swagger.io/?url=http://localhost:8080/v2/api-docs'
  },
  {
    title: 'FEATURES',
    group: true,
  },
  {
    title: 'Auth',
    icon: 'nb-locked',
    children: [
      {
        title: 'Login',
        link: '/auth/login',
      },
      {
        title: 'Register',
        link: '/auth/register',
      },
      {
        title: 'Request Password',
        link: '/auth/request-password',
      },
      {
        title: 'Reset Password',
        link: '/auth/reset-password',
      },
    ],
  },
];
