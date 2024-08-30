import type { ConfigModel } from '../../../app';

export default class ConfigManager {
	static get config(): ConfigModel {
		return globalThis.config;
	}

	static get backendUrl(): string {
		console.log(this.config);
		return this.config.backendUrl || location.origin;
	}
}
