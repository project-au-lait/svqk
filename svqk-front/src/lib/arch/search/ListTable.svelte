<script lang="ts" module>
  import type { Snippet } from 'svelte';

  export interface ListTableColumn<T> {
    label: string;
    sortKey: string;
    tdBody: (t: T) => string | Snippet<[T]>;
    tdClass?: string[];
  }

  export class ColumnsBuilder<T> {
    private columns: ListTableColumn<T>[] = [];

    build() {
      return this.columns;
    }

    add(
      label: string,
      sortKey: string,
      tdBody: (t: T) => string | Snippet<[T]>,
      tdClass?: string[]
    ) {
      this.columns.push({ label, sortKey, tdBody, tdClass });
      return this;
    }
  }
</script>

<script lang="ts" generics="T">
  import type { PageControlModel, PageResultModel, SortOrderModel } from '$lib/arch/api/Api';
  import Pagination from '$lib/arch/search/Pagination.svelte';
  import SortDirection from '$lib/arch/search/SortDirection.svelte';
  import { t } from '$lib/translations';

  interface Props {
    result: {
      list: T[];
      pageResult: PageResultModel;
    };
    columns: ListTableColumn<T>[];
    pageContol: PageControlModel;
    search: () => void;
  }

  let { result, columns, pageContol = $bindable(), search }: Props = $props();
</script>

{#if result.list.length}
  <section>
    <table class="list striped">
      <thead>
        <tr>
          {#each columns as col}
            {@const { label, sortKey } = col}
            <th>
              <SortDirection {label} {sortKey} bind:sortOrders={pageContol.sortOrders} {search} />
            </th>
          {/each}
        </tr>
      </thead>
      <tbody>
        {#each result.list as item}
          <tr>
            {#each columns as col}
              {@const data = col.tdBody(item)}
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
    <Pagination pageResult={result.pageResult} bind:pageNumber={pageContol.pageNumber} {search} />
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
