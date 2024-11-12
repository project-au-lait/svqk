import { IssueStoreMaster } from '$lib/domain/issue/IssueMasterStore';
import { loadTranslations } from '$lib/translations';
import type { LayoutLoad } from './$types';

export const ssr = false;

export const load: LayoutLoad = async ({ fetch }) => {
  const userLang = navigator.language;
  await loadTranslations(userLang);

  await IssueStoreMaster.loadAll(fetch);

  return {};
};
