<script lang="ts">
  import { invalidateAll } from '$app/navigation';
  import <%= entityNmPascal %>Form from '$lib/domain/<%= entityNmPlural %>/<%= entityNmPascal %>Form.svelte';
  import { t } from '$lib/translations';
  import type { PageData } from './$types';

  let { data }: { data: PageData } = $props();
  let <%= entityNmCamel %> = $derived(data.<%= entityNmCamel %>);

  async function handleAfterSave(id?: number) {
    await invalidateAll();
  }
</script>

<<%= entityNmPascal %>Form {<%= entityNmCamel %>} {handleAfterSave} actionBtnLabel={$t('msg.update')} />
