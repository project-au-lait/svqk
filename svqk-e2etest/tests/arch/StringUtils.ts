export default class StringUtils {
    static generateRandomString(): string {
        return Math.random().toString(36).slice(2);
    }
}
  