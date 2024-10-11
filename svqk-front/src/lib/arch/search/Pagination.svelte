<script lang="ts">
  export let result: {
    start: number;
    end: number;
    count: number;
    lastPage: number;
    pageNums: number[];
  };
  export let currentPage = 1;
  export let handlePage: (page: number) => Promise<void>;

  async function gotoPage(page: number) {
    currentPage = page;
    await handlePage(page);
  }
</script>

<div style="display: flex; justify-content: end;">
  <span>( {result.start}-{result.end} / {result.count} )</span>
</div>

{#if result.lastPage > 1}
  <div class="page" role="group">
    <button class="outline" disabled={currentPage <= 1} on:click={() => gotoPage(1)}> « </button>

    <button class="outline" disabled={currentPage <= 1} on:click={() => gotoPage(currentPage - 1)}>
      &lt;
    </button>

    {#each result.pageNums as page}
      <button class:outline={page != currentPage} on:click={() => gotoPage(page)}>
        {page}
      </button>
    {/each}

    <button
      class="outline"
      disabled={currentPage >= result.lastPage}
      on:click={() => gotoPage(currentPage + 1)}
    >
      &gt;
    </button>

    <button
      class="outline"
      disabled={currentPage >= result.lastPage}
      on:click={() => gotoPage(result.lastPage)}
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
