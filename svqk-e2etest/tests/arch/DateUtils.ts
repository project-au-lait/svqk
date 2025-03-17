import { LocalDate, LocalDateTime } from '@api/Api';

export default class DateUtils {
  static generateRandomDate(): Date {
    return new Date(Math.floor(Math.random() * new Date().getTime()));
  }

  static generateRandomLocalDateTime(): LocalDate {
    return DateUtils.generateRandomDate().toISOString();
  }

  static generateRandomLocalDate(): LocalDateTime {
    return DateUtils.generateRandomDate().toISOString().split('T')[0];
  }
}
