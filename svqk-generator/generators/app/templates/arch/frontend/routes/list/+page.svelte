<%_ include('../../../../lib/typescript-common'); -%>
<script lang="ts">
  import { goto } from '$app/navigation';
  import type { <%= entityNmPascal %>Model } from '$lib/arch/api/Api';
  import FormValidator from '$lib/arch/form/FormValidator';
  import CriteriaUtils from '$lib/arch/search/CriteriaUtils';
  import ListTable, { ColumnsBuilder } from '$lib/arch/search/ListTable.svelte';
  import type { PageProps } from '../<%= entityNmPlural %>/$types';
  import * as m from '$lib/paraglide/messages';

  let { data }: PageProps = $props();
  let { criteria } = $state(data);
  let { result } = $derived(data);

  const form = FormValidator.createForm({}, search);

  const columns = new ColumnsBuilder<<%= entityNmPascal %>Model>()
    .add('#', '<%= entityNmFirstLetter %>.id', () => <%= entityNmCamel %>IdAnchor)
    <%_ for (field of nonIdFields) { _%>
    <%- tscom.buildAddColumn(field) %>
    <%_ } _%>
    .build();

  function search() {
    goto(CriteriaUtils.encode(criteria));
  }
</script>

<section>
  <form use:form>
    <fieldset role="search">
      <input id="search" type="search" bind:value={criteria.text} />
      <input type="submit" value="Search" />
    </fieldset>
  </form>
</section>

<section>
  <a id="new<%= entityNmPascal %>" href="/<%= entityNmPlural %>/new"> {m.newEntity()} </a>
</section>

<section>
  <ListTable
    {result}
    {columns}
    bind:pageControl={criteria.pageControl}
    bind:sortOrders={criteria.sortOrders}
    {search} />
</section>

{#snippet <%= entityNmCamel %>IdAnchor(<%= entityNmCamel %>: <%= entityNmPascal %>Model)}
  <a href={`/<%= entityNmPlural %>/<%= tscom.idPath %>`}>{`<%= tscom.idPath %>`}</a>
{/snippet}
