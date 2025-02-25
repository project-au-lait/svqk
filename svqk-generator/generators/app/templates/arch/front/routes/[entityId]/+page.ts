import type { <%= entityNmPascal %>Model } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, params }) => {
  const <%= entityNmCamel %> = (await ApiHandler.handle<<%= entityNmPascal %>Model>(fetch, (api) =>
    api.<%= entityNmCamel %>.<%= entityNmCamel %>Detail(Number(params.id))
  ))!;

  return {
    title: <%= entityNmCamel %>.id,
    <%= entityNmCamel %>
  };
};
