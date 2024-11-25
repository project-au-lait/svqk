<script lang="ts">
  import type { JournalModel } from '$lib/arch/api/Api';
  import { t } from '$lib/translations';
  import { marked } from 'marked';

  interface Props {
    journals: JournalModel[];
  }

  let { journals }: Props = $props();

  marked.setOptions({ breaks: true });
</script>

{#if journals.length}
  <h3>{$t('msg.history')}</h3>
  {#each journals as journal, i}
    <article id={'note-' + (i + 1)}>
      <header><a href={'#note-' + (i + 1)}>#{i + 1}</a></header>
      <div>{@html marked(journal.notes)}</div>
    </article>
  {/each}
{/if}
