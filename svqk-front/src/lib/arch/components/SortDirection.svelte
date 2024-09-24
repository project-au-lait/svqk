<script lang="ts">
  import SortOrderUtils from '$lib/arch/search/SortOrderUtils';

  export let condition;
  export let label: string;
  export let sortKey: string;

  $: sortOrders = condition.sortOrders;
  $: sortMark = SortOrderUtils.getSortMark(sortOrders, sortKey);
</script>

<span>{label}{sortMark}</span>
