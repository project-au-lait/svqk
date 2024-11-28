<script lang="ts">
  interface Props {
    result: {
      start: number;
      end: number;
      count: number;
      lastPage: number;
      pageNums: number[];
    };
    currentPage?: number;
    dispPageRange?: number;
    handlePage: (page: number) => Promise<void>;
  }

  let {
    result,
    currentPage = $bindable(1),
    dispPageRange = $bindable(2),
    handlePage
  }: Props = $props();

  let dispPageNums: number[] = $derived(
    result.pageNums.slice(Math.max(currentPage - dispPageRange - 1, 0), currentPage + dispPageRange)
  );

  async function gotoPage(page: number) {
    currentPage = page;
    await handlePage(page);
  }
</script>

<div style="display: flex; justify-content: end;">
  <span>( {result.start}-{Math.max(0, result.end)} / {result.count} )</span>
</div>

{#if result.lastPage > 1}
  <div class="page" role="group">
    <button class="outline" disabled={currentPage <= 1} onclick={() => gotoPage(1)}> « </button>

    <button class="outline" disabled={currentPage <= 1} onclick={() => gotoPage(currentPage - 1)}>
      &lt;
    </button>

    {#if (dispPageNums.slice(0, 1).pop() ?? 0) > 1}
      <button class="outline" disabled>...</button>
    {/if}

    {#each dispPageNums as page}
      <button class:outline={page != currentPage} onclick={() => gotoPage(page)}>
        {page}
      </button>
    {/each}

    {#if (dispPageNums.slice(-1).pop() ?? result.lastPage) < result.lastPage}
      <button class="outline" disabled>...</button>
    {/if}

    <button
      class="outline"
      disabled={currentPage >= result.lastPage}
      onclick={() => gotoPage(currentPage + 1)}
    >
      &gt;
    </button>

    <button
      class="outline"
      disabled={currentPage >= result.lastPage}
      onclick={() => gotoPage(result.lastPage)}
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
