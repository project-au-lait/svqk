import { loadTranslations } from '$lib/translations';
import type { LayoutLoad } from './$types';

export const ssr = false;

export const load: LayoutLoad = async () => {
  const userLang = navigator.language;
  await loadTranslations(userLang);

  return {};
};
