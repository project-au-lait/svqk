<script lang="ts">
  import type { PageProps } from './$types';
  import * as m from '$lib/paraglide/messages';
  import { issueStatuses } from '$lib/domain/issue/IssueStatusMasterStore';
  import { trackers } from '$lib/domain/issue/TrackerMasterStore';

  let { data }: PageProps = $props();
  let { issueTracking } = data;
</script>

<article>
  <header><h3><strong>{m.issueTracking()}</strong></h3></header>
  <table class="striped">
    <thead>
      <tr>
        <th id="#"></th>
        {#each $issueStatuses as issueStatus}
          <th id="status">{issueStatus.name}</th>
        {/each}
        <th id="total">{m.total()}</th>
      </tr>
    </thead>
    <tbody>
      {#each $trackers as tracker}
        <tr>
          <th id="tracker" class="row">{tracker.name}</th>
          {#each $issueStatuses as issueStatus}
            <td>{issueTracking.trackerStatusCountMap[tracker.id][issueStatus.id]}</td>
          {/each}
          <td>{issueTracking.trackerCountMap[tracker.id]}</td>
        </tr>
      {/each}
    </tbody>
  </table>
  <footer><a href="/issues">{m.viewAllIssues()}</a></footer>
</article>

<style>
  table th:not(.row),
  td {
    text-align: center;
  }
</style>
