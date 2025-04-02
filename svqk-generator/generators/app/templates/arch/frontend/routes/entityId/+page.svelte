<%_ include('../../../../lib/typescript-common'); -%>
<script lang="ts">
  import { goto } from '$app/navigation';
  import { invalidateAll } from '$app/navigation';
  import <%= entityNmPascal %>Form from '$lib/domain/<%= entityNmPlural %>/<%= entityNmPascal %>Form.svelte';
  import { t } from '$lib/translations';
  import type { PageData } from './$types';

  let { data }: { data: PageData } = $props();
  let <%= entityNmCamel %> = $derived(data.<%= entityNmCamel %>);
  let new<%= entityNmPascal %> = $state(data.<%= entityNmCamel %>);
  $effect(() => {
    new<%= entityNmPascal %> = { ...<%= entityNmCamel %>, version: <%= entityNmCamel %>.version };
  });

  async function handleAfterSave(id?: <%= tscom.idType %>) {
    await invalidateAll();
  }

  async function handleAfterDelete() {
    await goto(`/<%= entityNmPlural %>/`);
  }
</script>

<<%= entityNmPascal %>Form bind:<%= entityNmCamel %>={new<%= entityNmPascal %>} updateMode={true} {handleAfterSave} {handleAfterDelete} />
