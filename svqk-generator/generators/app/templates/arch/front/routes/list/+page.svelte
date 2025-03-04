<script lang="ts">
  import { goto } from '$app/navigation';
  import type { <%= entityNmPascal %>Model } from '$lib/arch/api/Api';
  import FormValidator from '$lib/arch/form/FormValidator';
  import ListTable, { ColumnsBuilder } from '$lib/arch/search/ListTable.svelte';
  import type { PageData } from '../<%= entityNmPlural %>/$types';
  import { t } from '$lib/translations';

  let { data }: { data: PageData } = $props();
  let { condition } = $state(data);
  let { result } = $derived(data);

  const form = FormValidator.createForm({}, search);

  const columns = new ColumnsBuilder<<%= entityNmPascal %>Model>().add('#', 'i.id', () => <%= entityNmCamel %>IdAnchor).build();

  function search() {
    goto(`?q=${encodeURIComponent(JSON.stringify(condition))}`);
  }
</script>

<section>
  <form use:form>
    <fieldset role="search">
      <input type="search" bind:value={condition.text} />
      <input type="submit" value="Search" />
    </fieldset>
  </form>
</section>

<section>
  <a id="new<%= entityNmPascal %>" href="/<%= entityNmPlural %>/new"> {$t('msg.newEntity')} </a>
</section>

<section>
  <ListTable {result} {columns} bind:pageControl={condition.pageControl} {search} />
</section>

{#snippet <%= entityNmCamel %>IdAnchor(<%= entityNmCamel %>: <%= entityNmPascal %>Model)}
  <a href={`/<%= entityNmPlural %>/${<%= entityNmCamel %>.id}`}>{<%= entityNmCamel %>.id}</a>
{/snippet}
