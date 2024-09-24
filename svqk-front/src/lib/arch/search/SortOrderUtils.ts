import type { SortOrderModel } from '../api/Api';

export default class SortOrderUtils {
  static addSort(sortOrders: SortOrderModel[] | undefined, field: string) {
    if (!sortOrders) {
      sortOrders = [];
    }

    const index = sortOrders.findIndex((sortOrder) => sortOrder.field == field);

    if (index == -1) {
      sortOrders.unshift({ field, asc: true } as SortOrderModel);
    } else {
      sortOrders[index].asc = !sortOrders[index].asc;
      const [existingSortOrder] = sortOrders.splice(index, 1);
      sortOrders.unshift(existingSortOrder);
    }

    if (sortOrders.length > 2) {
      sortOrders.pop();
    }

    return sortOrders;
  }

  static getSortMark(sortOrders: SortOrderModel[] | undefined, key: string) {
    if (!sortOrders || sortOrders.length === 0) {
      return '';
    }

    const order = sortOrders[0];
    if (order.field === key) {
      return order.asc ? '↑' : '↓';
    }

    return '';
  }
}
