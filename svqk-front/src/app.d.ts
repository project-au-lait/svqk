// See https://kit.svelte.dev/docs/types#app
// for information about these interfaces
declare global {
	namespace App {
		// interface Error {}
		// interface Locals {}
		// interface PageData {}
		// interface PageState {}
		// interface Platform {}
	}
}

export {};

export interface ConfigModel {
	backendUrl: string;
}
declare global {
	// eslint-disable-next-line no-var
	var config: ConfigModel;
}
