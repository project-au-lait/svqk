<script lang="ts">
  import type { SortOrderModel } from '$lib/arch/api/Api';
  import SortDirection from '../components/SortDirection.svelte';

  interface Props {
    label: string;
    key: string;
    children?: any;
    mode?: string;
    sortOrders?: SortOrderModel[];
    handleSort?: (field: string) => void;
  }

  let { label, key, children, mode, sortOrders, handleSort = (s: string) => {} }: Props = $props();
</script>

{#if mode == 'header'}
  <th>
    <SortDirection {label} sortKey={key} {sortOrders} {handleSort} />
  </th>
{:else if mode == 'data'}
  <!-- TODO add class -->
  <td>
    {@render children()}
  </td>
{/if}

<style>
  th,
  td:not(.align-left) {
    text-align: center;
    white-space: nowrap;
  }
</style>
