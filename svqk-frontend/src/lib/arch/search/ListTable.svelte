<script lang="ts" module>
  import type { Snippet } from 'svelte';

  export interface ListTableColumn<T> {
    label: string;
    sortKey: string;
    tdBody: (t: T) => Snippet<[T]> | any;
    tdClass?: string[];
  }

  export class ColumnsBuilder<T> {
    private columns: ListTableColumn<T>[] = [];

    build() {
      return this.columns;
    }

    add(label: string, sortKey: string, tdBody: (t: T) => Snippet<[T]> | any, tdClass?: string[]) {
      this.columns.push({ label, sortKey, tdBody, tdClass });
      return this;
    }
  }
</script>

<script lang="ts" generics="T">
  import type { PageControl, PageResult, SortOrder } from '$lib/arch/api/Api';
  import Pagination from '$lib/arch/search/Pagination.svelte';
  import SortDirection from '$lib/arch/search/SortDirection.svelte';
  import { t } from '$lib/translations';

  interface Props {
    result: {
      list?: T[];
      pageResult?: PageResult;
    };
    columns: ListTableColumn<T>[];
    pageControl: PageControl;
    sortOrders?: SortOrder[];
    search: () => void;
  }

  let {
    result,
    columns,
    pageControl = $bindable(),
    sortOrders = $bindable(),
    search
  }: Props = $props();
</script>

{#if result.list?.length}
  <section>
    <table class="list striped">
      <thead>
        <tr>
          {#each columns as col}
            {@const { label, sortKey } = col}
            <th>
              <SortDirection {label} {sortKey} bind:sortOrders {search} />
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
                {#if typeof data === 'function'}
                  {@render data(item)}
                {:else}
                  {@html data}
                {/if}
              </td>
            {/each}
          </tr>
        {/each}
      </tbody>
    </table>
  </section>

  <section>
    <Pagination pageResult={result.pageResult} bind:pageNumber={pageControl.pageNumber} {search} />
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
