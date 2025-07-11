import type { IssueTrackingModel } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import type { PageLoad } from './$types';
import * as m from '$lib/paraglide/messages';

export const load: PageLoad = async ({ fetch }) => {
  const issueTracking = (await ApiHandler.handle<IssueTrackingModel>(fetch, (api) =>
    api.issues.getTracking()
  ))!;

  return {
    title: m.home(),
    issueTracking
  };
};
