import { deLocalizeUrl } from '$lib/paraglide/runtime';
// Modules to be registered in the DI container
import '$lib/domain/issue/IssueStatusMasterStore'; // <.>
import '$lib/domain/issue/TrackerMasterStore';

export const reroute = (request) => deLocalizeUrl(request.url).pathname;
