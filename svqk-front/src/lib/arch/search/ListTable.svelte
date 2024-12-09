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

    build() {
      return this.columns;
    }

    add(
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
  import type { PageControlModel, SortOrderModel } from '$lib/arch/api/Api';
  import SortDirection from '$lib/arch/components/SortDirection.svelte';
  import Pagination from '$lib/arch/search/Pagination.svelte';
  import { t } from '$lib/translations';

  interface Props {
    list: T[];
    columns: ResultListColumn<T>[];
    sortOrders?: SortOrderModel[];
    pageCtrl: PageControlModel;
    search: (cond?: object) => void;
  }

  let { list, columns, sortOrders, pageCtrl, search }: Props = $props();
</script>

{#if list.length}
  <section>
    <table class="list striped">
      <thead>
        <tr>
          {#each columns as col}
            {@const { label, sortKey } = col}
            <th>
              <SortDirection {label} {sortKey} {sortOrders} {search} />
            </th>
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
    <Pagination {pageCtrl} {search} />
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
