<script lang="ts" module>
  import type { Snippet } from 'svelte';

  export interface ResultListColumn<T> {
    label: string;
    sortKey: string;
    getData: (t: T) => string | Snippet<[T]>;
    tdClass?: string[];
  }

  export class ColumnsBuilder<T> {
    private columns: ResultListColumn<T>[] = [];

    getColumns() {
      return this.columns;
    }

    addColumn(
      label: string,
      sortKey: string,
      getData: (t: T) => string | Snippet<[T]>,
      tdClass?: string[]
    ) {
      this.columns.push({ label, sortKey, getData, tdClass });
      return this;
    }
  }
</script>

<script lang="ts" generics="T">
  import type { SortOrderModel } from '$lib/arch/api/Api';
  import { t } from '$lib/translations';
  import SortDirection from '../components/SortDirection.svelte';
  import Pagination from './Pagination.svelte';

  interface Props {
    list: T[];
    columns: ResultListColumn<T>[];
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
    columns,
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
          {#each columns as col}
            {@const { label, sortKey } = col}
            <th><SortDirection {label} {sortKey} {sortOrders} {handleSort} /></th>
          {/each}
        </tr>
      </thead>
      <tbody>
        {#each list as item}
          <tr>
            {#each columns as col}
              {@const data = col.getData(item)}
              <td class={col.tdClass?.join(' ')}>
                {#if typeof data === 'string'}
                  {@html data}
                {:else}
                  {@render data(item)}
                {/if}
              </td>
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
