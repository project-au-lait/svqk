<script lang="ts">
  import { messageStore } from '$lib/arch/global/MessageStore';
  import '@picocss/pico/css/pico.min.css';
  import '@picocss/pico/css/pico.colors.min.css';
  import { t } from '$lib/translations';
  import { page } from '$app/stores';
  interface Props {
    children?: import('svelte').Snippet;
  }

  let { children }: Props = $props();
</script>

<svelte:head>
  <title>{$page.data.title} - SVQK</title>
</svelte:head>

<nav class="container">
  <ul>
    <li><strong>SVQK</strong></li>
  </ul>
  <ul>
    <a href="/">{$t('msg.home')}</a>
    <li>
      <a href="about:blank">{$t('msg.project')}</a>
    </li>
    <li>
      <a href="about:blank">{$t('msg.activity')}</a>
    </li>
    <li>
      <a id="issue" href="/issues">{$t('msg.issue')}</a>
    </li>
    <!-- entityListLink -->
  </ul>
</nav>

<main class="container">
  <h1>{$page.data.title}</h1>

  {#if $messageStore.display}
    <article class="message">
      <span id="globalMessage"> {$messageStore.text}</span>
      <button class="close" onclick={() => messageStore.hide()}></button>
    </article>
  {/if}
  {@render children?.()}
</main>

<style lang="scss">
  :global(:root) {
    --pico-font-size: small;
  }

  :global(table.list) {
    width: 100%;
    :global(th) {
      text-align: center;
      white-space: nowrap;
    }
  }

  nav {
    max-width: 800px;
  }

  main {
    max-width: 700px;
  }

  article.message {
    display: flex;
    justify-content: space-between;
  }

  button.close {
    background-image: var(--pico-icon-close);
    background-color: transparent;
    border: none;
    background-size: auto 1rem;
  }
</style>
