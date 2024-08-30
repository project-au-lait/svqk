<script lang="ts">
  export let currentPage = 1;
  export let lastPage = 1;
  export let maxPageRange = 9;
  export let changePageFunc = (page: number) => {
    currentPage = page;
  };

  $: pageRange = () => {
    let maxRange = Math.min(lastPage, maxPageRange);
    let start = Math.max(currentPage - Math.ceil(maxRange / 2), 0);
    let end = Math.min(start + maxRange, lastPage);

    return [...Array(lastPage)]
      .map((_, i) => ++i)
      .slice(end - start < maxRange ? end - maxRange : start, end);
  };
</script>

<div class="page" role="group">
  <button class="outline" disabled={currentPage <= 1} on:click={() => changePageFunc(1)}>
    «
  </button>

  <button
    class="outline"
    disabled={currentPage <= 1}
    on:click={() => changePageFunc(currentPage - 1)}
  >
    &lt;
  </button>

  {#each pageRange() as page}
    <button class={page === currentPage ? '' : 'outline'} on:click={() => changePageFunc(page)}>
      {page}
    </button>
  {/each}

  <button
    class="outline"
    disabled={currentPage >= lastPage}
    on:click={() => changePageFunc(currentPage + 1)}
  >
    &gt;
  </button>

  <button
    class="outline"
    disabled={currentPage >= lastPage}
    on:click={() => changePageFunc(lastPage)}
  >
    »
  </button>
</div>

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
