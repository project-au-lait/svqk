import dayjs from 'dayjs';

export default class DateUtils {
  static date(date: string | undefined) {
    return this.format(date, 'YYYY/MM/DD');
  }

  static datetime(date: string | undefined) {
    return this.format(date, 'YYYY/MM/DD HH:mm');
  }

  private static format(date: string | undefined, format: string) {
    return date ? dayjs(date).format(format) : '';
  }
}
