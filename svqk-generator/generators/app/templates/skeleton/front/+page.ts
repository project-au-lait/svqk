import type { PageLoad } from './$types';
import { env } from '$env/dynamic/public';

export const load: PageLoad = async ({ fetch }) => {
  const response = await fetch(`${env.PUBLIC_BACKEND_URL}/api/<%= entityNmCamel %>/1`);
  const <%= entityNmCamel %> = await response.json();

  return {
    title: '<%= entityNmPascal %> Entity',
    status: response.status,
    <%= entityNmCamel %>
  };
};
