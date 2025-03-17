export default class BooleanUtils {
  static generateRandomBoolean(): boolean {
    return Math.random() < 0.5;
  }
}
