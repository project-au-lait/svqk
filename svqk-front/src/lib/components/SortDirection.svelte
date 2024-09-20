<script lang="ts">
  import type { IssueSearchConditionModel } from '$lib/arch/api/Api';
  import SortOrderUtils from '$lib/arch/search/SortOrderUtils';

  export let condition: IssueSearchConditionModel;
  export let label: string;
  export let sortKey: string;
  export let search: () => Promise<void>;

  $: sortMark = SortOrderUtils.getSortMark(condition.sortOrders, sortKey);

  async function onClick() {
    condition.sortOrders = SortOrderUtils.addSort(condition.sortOrders, sortKey);
    await search();
  }
</script>

<th on:click={onClick}>
  <span>{label}{sortMark}</span>
</th>

<style>
  th span {
    cursor: pointer;
  }
</style>
