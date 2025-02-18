<script lang="ts">
  import type { SortOrderModel } from '$lib/arch/api/Api';
  import SortOrderUtils from '$lib/arch/search/SortOrderUtils';

  interface Props {
    label: string;
    sortKey: string;
    sortOrders?: SortOrderModel[];
    search: () => void;
  }

  let { label, sortKey, sortOrders = $bindable(), search }: Props = $props();

  let sortMark = $derived(SortOrderUtils.getSortMark(sortOrders, sortKey));

  function onClick() {
    sortOrders = SortOrderUtils.addSort(sortOrders, sortKey);
    search();
  }
</script>

<a href={undefined} onclick={onClick}>{label}{sortMark}</a>

<style>
  a {
    cursor: pointer;
    text-decoration: none;
  }
</style>
