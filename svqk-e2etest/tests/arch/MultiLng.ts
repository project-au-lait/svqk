import { osLocaleSync } from 'os-locale';
import { setLocale } from '@paraglide/runtime';

export const locale = osLocaleSync().split('-')[0];

// export const locale = 'en';
console.log(`Runtime locale: ${locale}`);

if (locale === 'en' || locale === 'ja') {
  setLocale(locale);
} else {
  setLocale('en');
}
