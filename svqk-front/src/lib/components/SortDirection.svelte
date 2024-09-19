<script lang="ts">
  import { createEventDispatcher } from 'svelte';
  import SortOrderUtils from '$lib/arch/search/SortOrderUtils';

  export let sortOrders;
  export let label;
  export let sortKey: string;

  const dispatch = createEventDispatcher();

  $: sortMark = SortOrderUtils.getSortMark(sortOrders, sortKey);

  function onClick() {
    dispatch('sort', { sortKey });
  }
</script>

<th on:click={onClick}>
  <span>{label}{sortMark}</span>
</th>
