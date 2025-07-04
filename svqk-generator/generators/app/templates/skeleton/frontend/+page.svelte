<script lang="ts">
  import type { PageProps } from './$types';

  let { data }: PageProps = $props();
  const entries = Object.entries(data.<%= entityNmCamel %>).map(([key, value]) => ({key, value}))
</script>

<table>
  <thead>
    <tr>
      {#each entries as entry}
        <th>
          {entry.key}
        </th>
      {/each}
    </tr>
  </thead>
  <tbody>
    <tr>
      {#each entries as entry}
        <td>
          {entry.value}
        </td>
      {/each}
    </tr>
  </tbody>
</table>

<div>HTTP status: <span id="status">{data.status}</span></div>

