import type { PageLoad } from './$types';
import { env } from '$env/dynamic/public';

export const load: PageLoad = async ({ fetch }) => {
  <%_
  const id = compIdFields ? compIdFields.map((field) => '1').join('/') : '1';
  -%>
  const response = await fetch(`${env.PUBLIC_BACKEND_URL}/api/<%= entityNmCamel %>/<%= id %>`);
  const <%= entityNmCamel %> = await response.json();

  return {
    title: '<%= entityNmPascal %> Entity',
    status: response.status,
    <%= entityNmCamel %>
  };
};
