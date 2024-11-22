<script lang="ts">
  import type { JournalModel } from '$lib/arch/api/Api';
  import { t } from '$lib/translations';

  interface Props {
    journals: JournalModel[];
  }

  let { journals }: Props = $props();

  // TODO for debug
  console.log(journals);
</script>

{#if journals.length}
  <h3>{$t('msg.history')}</h3>
  {#each journals.sort() as journal}
    <article>
      <header>#{journal.id}</header>
      <p>{journal.notes}</p>
    </article>
  {/each}
{/if}