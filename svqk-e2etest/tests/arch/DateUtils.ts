import { LocalDate, LocalDateTime } from '@api/Api';

export default class DateUtils {
  static generateRandomDate(): Date {
    return new Date(Math.floor(Math.random() * new Date().getTime()));
  }

  static generateRandomLocalDateTime(): LocalDateTime {
    return DateUtils.generateRandomDate().toISOString().slice(0, 19);
  }

  static generateRandomLocalDate(): LocalDate {
    return DateUtils.generateRandomDate().toISOString().split('T')[0];
  }
}
