<script lang="ts">
  import { pageStore } from '$lib/arch/global/PageStore';
  import type { PageData } from './$types';
  import { t } from '$lib/translations';

  export let data: PageData;

  let { issueTracking } = data;

  pageStore.setTitle($t('msg.home'));
</script>

<article>
  <header><h3><strong>{$t('msg.issueTracking')}</strong></h3></header>
  <table class="striped">
    <thead>
      <tr>
        <th />
        {#each issueTracking[0].issueStatuses as is}
          <th>{is.issueStatus.name}</th>
        {/each}
        <th>{$t('msg.total')}</th>
      </tr>
    </thead>
    <tbody>
      {#each issueTracking as it}
        <tr>
          <th class="row">{it.tracker.name}</th>
          {#each it.issueStatuses as is}
            <td>{is.count}</td>
          {/each}
          <td>{it.total}</td>
        </tr>
      {/each}
    </tbody>
  </table>
  <footer><a href="/issues">{$t('msg.viewAllIssues')}</a></footer>
</article>

<style>
  table th:not(.row),
  td {
    text-align: center;
  }
</style>
