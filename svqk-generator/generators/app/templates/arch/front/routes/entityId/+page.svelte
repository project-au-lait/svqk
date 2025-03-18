<%_ include('../../../../lib/frontend-common', { entityNmPascal, compIdFields }); -%>
<script lang="ts">
  import { goto } from '$app/navigation';
  import { invalidateAll } from '$app/navigation';
  import <%= entityNmPascal %>Form from '$lib/domain/<%= entityNmPlural %>/<%= entityNmPascal %>Form.svelte';
  <%- importDecIdType %>
  import { t } from '$lib/translations';
  import type { PageData } from './$types';

  let { data }: { data: PageData } = $props();
  let <%= entityNmCamel %> = $derived(data.<%= entityNmCamel %>);

  async function handleAfterSave(id?: <%= idType %>) {
    await invalidateAll();
  }

  async function handleAfterDelete() {
    await goto(`/<%= entityNmPlural %>/`);
  }
</script>

<<%= entityNmPascal %>Form {<%= entityNmCamel %>} {handleAfterSave} {handleAfterDelete} />
