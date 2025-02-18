import i18n from 'sveltekit-i18n';

const config = {
  loaders: [
    {
      locale: 'en',
      key: 'msg',
      loader: async () => (await import('./en.json')).default
    },
    {
      locale: 'ja',
      key: 'msg',
      loader: async () => (await import('./ja.json')).default
    }
  ],
  fallbackLocale: 'en'
};

export const { t, locale, locales, loading, loadTranslations } = new i18n(config);
