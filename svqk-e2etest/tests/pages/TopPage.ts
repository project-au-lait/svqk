import BasePage from "./BasePage";

export default class TopPage extends BasePage {
    get pageName() {
        return 'トップ画面';
    }

    async gotoTop() {
        await this.goTo('/');
    }
}