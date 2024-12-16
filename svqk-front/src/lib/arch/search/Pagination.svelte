<script lang="ts">
  import type { PageControlModel } from '$lib/arch/api/Api';

  interface Props {
    pageCtrl: PageControlModel;
    pageNumber?: number;
    search: () => void;
  }

  let { pageCtrl, pageNumber = $bindable(1), search }: Props = $props();
  let { count, start, end, pageNums, lastPage } = $derived(pageCtrl);

  function gotoPage(page: number) {
    pageNumber = page;
    search();
  }
</script>

<div style="display: flex; justify-content: end;">
  <span>( {start}-{Math.max(0, end)} / {count} )</span>
</div>

{#if lastPage > 1}
  <div class="page" role="group">
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

    {#if (pageNums.slice(-1).pop() ?? lastPage) < lastPage}
      <button class="outline" disabled>...</button>
    {/if}

    <button
      class="outline"
      disabled={pageNumber >= lastPage}
      onclick={() => gotoPage(pageNumber + 1)}
    >
      &gt;
    </button>

    <button class="outline" disabled={pageNumber >= lastPage} onclick={() => gotoPage(lastPage)}>
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
