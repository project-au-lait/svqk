import type {
  IssueSearchConditionModel,
  IssueSearchResultModel,
  IssueStatusModel,
  TrackerModel
} from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch }) => {
  const condition = { issueStatuses: [] } as IssueSearchConditionModel; // <.>

  const result =
    (await ApiHandler.handle<IssueSearchResultModel>(fetch, (api) =>
      api.issues.issuesSearch(condition)
    )) || ({} as IssueSearchResultModel); // <.>

  const issueStatuses = (await ApiHandler.handle<IssueStatusModel[]>(fetch, (api) =>
    api.issueStatuses.issueStatusesList()
  ))!;

  const trackers = (await ApiHandler.handle<TrackerModel[]>(fetch, (api) =>
    api.tracker.trackerList()
  ))!;

  return {
    condition,
    issueStatuses,
    trackers,
    result
  }; // <.>
};
