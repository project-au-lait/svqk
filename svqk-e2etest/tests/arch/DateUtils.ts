import { LocalDate, LocalDateTime } from '@api/Api';

export default class DateUtils {
  static generateRandomDate(): Date {
    return new Date(Math.floor(Math.random() * new Date().getTime()));
  }

  static generateRandomLocalDateTime(): LocalDate {
    const date = new Date(
      Date.now() - Math.floor(Math.random() * 10 * 365 * 24 * 60 * 60 * 1000) // 約10年分の範囲でランダム
    );

    const pad = (n: number) => n.toString().padStart(2, '0');
    const yyyy = date.getFullYear();
    const mm = pad(date.getMonth() + 1);
    const dd = pad(date.getDate());
    const hh = pad(date.getHours());
    const min = pad(date.getMinutes());

    return `${yyyy}-${mm}-${dd}T${hh}:${min}`;
  }

  static generateRandomLocalDate(): LocalDateTime {
    return DateUtils.generateRandomDate().toISOString().split('T')[0];
  }
}
