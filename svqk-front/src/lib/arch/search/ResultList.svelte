<script lang="ts" generics="T">
  import type { SortOrderModel } from '$lib/arch/api/Api';
  import { t } from '$lib/translations';
  import { type Snippet } from 'svelte';
  import Pagination from './Pagination.svelte';

  interface Props {
    list: T[];
    columns: Snippet<[T, string, SortOrderModel[]?, ((s: string) => void)?]>;
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
          {@render columns({} as T, 'header', sortOrders, handleSort)}
        </tr>
      </thead>
      <tbody>
        {#each list as item}
          <tr>
            {@render columns(item, 'data')}
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
