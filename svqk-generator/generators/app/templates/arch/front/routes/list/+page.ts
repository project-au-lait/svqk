import type { <%= entityNmPascal %>SearchCriteriaModel, <%= entityNmPascal %>SearchResultModel } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, url }) => {
  const condition = {
    pageControl: {
      pageNumber: 1
    },
    ...JSON.parse(decodeURIComponent(url.searchParams.get('q') ?? '{}'))
  } as <%= entityNmPascal %>SearchCriteriaModel;

  const result =
    (await ApiHandler.handle<<%= entityNmPascal %>SearchResultModel>(fetch, (api) =>
      api.<%= entityNmCamel %>.<%= entityNmCamel %>Search(condition)
    )) || ({} as <%= entityNmPascal %>SearchResultModel);

  return {
    condition,
    result
  };
};
