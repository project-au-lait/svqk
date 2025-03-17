<script lang="ts">
  import type { PageResult } from '$lib/arch/api/Api';

  interface Props {
    pageResult?: PageResult;
    pageNumber?: number;
    search: () => void;
  }

  const INITIAL_PAGE_RESULT: PageResult = {
    count: 0,
    start: 0,
    end: 0,
    pageNums: [],
    lastPageNum: 0
  };

  let { pageResult, pageNumber = $bindable(1), search }: Props = $props();
  pageResult = pageResult ?? INITIAL_PAGE_RESULT;

  let { count, start, end, pageNums, lastPageNum } = $derived(pageResult);

  function gotoPage(page: number) {
    pageNumber = page;
    search();
  }
</script>

<div style="display: flex; justify-content: end;">
  <span>( {start}-{Math.max(0, end)} / {count} )</span>
</div>

{#if lastPageNum > 1}
  <!-- prettier-ignore -->
  <div class="page" role="group"><!--//NOSONAR-->
    <button class="outline" disabled={pageNumber <= 1} onclick={() => gotoPage(1)}> « </button>

    <button class="outline" disabled={pageNumber <= 1} onclick={() => gotoPage(pageNumber - 1)}>
      &lt;
    </button>

    {#if (pageNums.slice(0, 1).pop() ?? 0) > 1}
      <button class="outline" disabled>...</button>
    {/if}

    {#each pageNums as page}
      <button class:outline={page != pageNumber} onclick={() => gotoPage(page)}>
        {page}
      </button>
    {/each}

    {#if (pageNums.slice(-1).pop() ?? lastPageNum) < lastPageNum}
      <button class="outline" disabled>...</button>
    {/if}

    <button
      id="nextPageButton"
      class="outline"
      disabled={pageNumber >= lastPageNum}
      onclick={() => gotoPage(pageNumber + 1)}
    >
      &gt;
    </button>

    <button
      class="outline"
      disabled={pageNumber >= lastPageNum}
      onclick={() => gotoPage(lastPageNum)}
    >
      »
    </button>
  </div>
{/if}

<style>
  .page {
    display: flex;
    justify-content: center;
    box-shadow: none;
  }

  .page button {
    padding: 0.5rem 1.5rem;
    max-width: max-content;
  }
</style>
