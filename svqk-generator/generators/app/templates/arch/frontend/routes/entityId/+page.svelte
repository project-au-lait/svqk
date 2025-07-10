<%_ include('../../../../lib/typescript-common'); -%>
<script lang="ts">
  import { goto } from '$app/navigation';
  import { invalidateAll } from '$app/navigation';
  import <%= entityNmPascal %>Form from '$lib/domain/<%= entityNmPlural %>/<%= entityNmPascal %>Form.svelte';
  import type { PageProps } from './$types';

  let { data }: PageProps = $props();
  let <%= entityNmCamel %> = $state(data.<%= entityNmCamel %>);
  $effect(() => {
    <%= entityNmCamel %> = data.<%= entityNmCamel %>;
  });

  async function handleAfterSave(id?: <%= tscom.idType %>) {
    await invalidateAll();
  }

  async function handleAfterDelete() {
    await goto(`/<%= entityNmPlural %>/`);
  }
</script>

<<%= entityNmPascal %>Form bind:<%= entityNmCamel %> updateMode={true} {handleAfterSave} {handleAfterDelete} />
