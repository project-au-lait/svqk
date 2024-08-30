import type { PageLoad } from './$types';
import { env } from '$env/dynamic/public';

// <.>
export const load: PageLoad = async ({ fetch }) => {
  const response = await fetch(`${env.PUBLIC_BACKEND_URL}/api/v1/hello/1`);
  const hello = await response.json();

  // <.>
  return {
    hello
  };
};
