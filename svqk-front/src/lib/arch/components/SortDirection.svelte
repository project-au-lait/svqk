<script lang="ts">
  import SortOrderUtils from '$lib/arch/search/SortOrderUtils';
  import type { SortOrderModel } from '$lib/arch/api/Api';

  export let sortOrders: SortOrderModel[] = [];
  export let label: string;
  export let sortKey: string;
  export let handleSort: (field: string) => void;

  $: sortMark = SortOrderUtils.getSortMark(sortOrders, sortKey);

  function onClick() {
    handleSort(sortKey);
  }
</script>

<a href="#/" on:click={onClick}>{label}{sortMark}</a>

<style>
  a {
    text-decoration: none
  }
</style>