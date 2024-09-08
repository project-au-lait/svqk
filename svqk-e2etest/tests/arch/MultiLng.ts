import i18next from 'i18next';
import Backend from 'i18next-fs-backend';
import { osLocaleSync } from 'os-locale';

export const locale = osLocaleSync().split('-')[0];

// export const locale = 'en';
console.log(`Runtime locale: ${locale}`);

await i18next.use(Backend).init(
  {
    lng: locale,
    fallbackLng: 'en',
    debug: false,
    backend: {
      loadPath: 'tests/translations/{{lng}}.json'
    }
  }
  // ,
  // (err) => {
  //   if (err) {
  //     console.error(err);
  //   }
  // }
);

export function t(key: string) {
  return i18next.t(key);
}
