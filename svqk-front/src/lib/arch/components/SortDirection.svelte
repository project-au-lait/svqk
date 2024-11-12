<script lang="ts">
  import SortOrderUtils from '$lib/arch/search/SortOrderUtils';
  import type { SortOrderModel } from '$lib/arch/api/Api';

  interface Props {
    sortOrders?: SortOrderModel[];
    label: string;
    sortKey: string;
    handleSort: (field: string) => void;
  }

  let {
    sortOrders = [],
    label,
    sortKey,
    handleSort
  }: Props = $props();

  let sortMark = $derived(SortOrderUtils.getSortMark(sortOrders, sortKey));

  function onClick() {
    handleSort(sortKey);
  }
</script>

<a href="#/" onclick={onClick}>{label}{sortMark}</a>

<style>
  a {
    text-decoration: none
  }
</style>