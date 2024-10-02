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

<button type="button" on:click={onClick} class="sort-button">
  {label}{sortMark}
</button>

<style>
  .sort-button {
    background: none;
    border: none;
    padding: 0;
    cursor: pointer;
    font: inherit;
    color: inherit;
    margin-bottom: 0;
  }
</style>
