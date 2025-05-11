<%_ include('../../../../lib/typescript-common'); -%>
import type { <%= entityNmPascal %>Model } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, params }) => {
  const <%= entityNmCamel %> = (await ApiHandler.handle<<%= entityNmPascal %>Model>(fetch, (api) =>
    <%- tscom.buildDetailApiCall(entityNmCamel, compIdFields) %>
  ))!;

  return {
    title: `<%= tscom.idPath %>`,
    <%= entityNmCamel %>
  };
};
