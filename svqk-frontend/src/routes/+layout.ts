import type { LayoutLoad } from './$types';
import { TYPES } from '$lib/arch/di/Types';
import { DIContainer } from '$lib/arch/di/Container';
import { LoadExecutor } from '$lib/arch/master/LoadExecutor';

export const ssr = false;

export const load: LayoutLoad = async ({ fetch }) => {
  const container = DIContainer.getInstance();
  const loadExecutor = container.get<LoadExecutor>(TYPES.LoadExecutor);
  await loadExecutor.loadAll(fetch);

  return {};
};
