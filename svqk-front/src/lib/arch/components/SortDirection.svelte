<script lang="ts">
  import SortOrderUtils from '$lib/arch/search/SortOrderUtils';
  import type { SortOrderModel } from '$lib/arch/api/Api';

  export let sortOrders: SortOrderModel[] = [];
  export let label: string;
  export let sortKey: string;

  $: sortMark = SortOrderUtils.getSortMark(sortOrders, sortKey);
</script>

<span>{label}{sortMark}</span>

<style>
  span {
    cursor: pointer;
  }
</style>
