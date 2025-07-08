<script lang="ts">
  import { page } from '$app/stores';
  import type { JournalModel } from '$lib/arch/api/Api';
  import * as m from '$lib/paraglide/messages';
  import { marked } from 'marked';
  import { onMount } from 'svelte';

  interface Props {
    journals: JournalModel[];
  }

  let { journals }: Props = $props();
  let hash = $state();

  const hashchange = () => (hash = $page.url.hash);
  const noteClass = (notehash: string) => (hash == notehash ? 'note-highlight' : '');

  onMount(hashchange);

  marked.setOptions({ breaks: true });
</script>

<svelte:window on:hashchange={hashchange} />

{#if journals.length}
  <h3>{m.history()}</h3>
  {#each journals as { seqNo, notes }}
    {@const noteId = `note-${seqNo}`}
    <article id={noteId} class={noteClass(`#${noteId}`)}>
      <header>
        <a href={`#${noteId}`}>#{seqNo}</a>
      </header>
      <div>{@html marked(notes ?? '')}</div>
    </article>
  {/each}
{/if}

<style>
  .note-highlight {
    border: 0.2rem solid rgb(1, 114, 173);
  }
</style>
