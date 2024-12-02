<script module lang="ts">
  export interface RlColDef<T> {
    label: string;
    sortKey: string;
    data: (t: T) => string | number;
  }
</script>

<script lang="ts" generics="T">
  import type { IssueModel, SortOrderModel } from '$lib/arch/api/Api';
  import { t } from '$lib/translations';
  import { type Snippet } from 'svelte';
  import Pagination from './Pagination.svelte';
  import SortDirection from '../components/SortDirection.svelte';

  interface Props {
    list: T[];
    colDefs: RlColDef<T>[];
    sortOrders?: SortOrderModel[];
    handleSort: (field: string) => void;
    currentPage?: number;
    handlePage: (page: number) => Promise<void>;
    result: {
      start: number;
      end: number;
      count: number;
      lastPage: number;
      pageNums: number[];
    };
  }

  let {
    list,
    colDefs,
    sortOrders,
    handleSort,
    currentPage = $bindable(),
    handlePage,
    result
  }: Props = $props();
</script>

{#if list.length}
  <section>
    <table class="list striped">
      <thead>
        <tr>
          {#each colDefs as colDef}
            {@const { label, sortKey } = colDef}
            <th><SortDirection {label} {sortKey} {sortOrders} {handleSort} /></th>
          {/each}
        </tr>
      </thead>
      <tbody>
        {#each list as item}
          <tr>
            {#each colDefs as colDef}
              <td>{@html colDef.data(item)}</td>
            {/each}
          </tr>
        {/each}
      </tbody>
    </table>
  </section>

  <section>
    <Pagination {result} {currentPage} {handlePage} />
  </section>
{:else}
  {$t('msg.noData')}
{/if}

<style>
  th,
  td:not(.align-left) {
    text-align: center;
    white-space: nowrap;
  }
</style>
