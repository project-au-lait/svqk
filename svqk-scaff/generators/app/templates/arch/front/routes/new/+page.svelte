<script lang="ts">
  import { goto } from '$app/navigation';
  import type { <%= entityNmPascal %>Model } from '$lib/arch/api/Api';
  import <%= entityNmPascal %>Form from '$lib/domain/<%= entityNmPlural %>/<%= entityNmPascal %>Form.svelte';
  import { t } from '$lib/translations';

  let <%= entityNmCamel %> = $state({} as <%= entityNmPascal %>Model);

  async function handleAfterSave(id?: number) {
    await goto(`/issues/${id}`);
  }
</script>

<<%= entityNmPascal %>Form bind:<%= entityNmCamel %> {handleAfterSave} actionBtnLabel={$t('msg.register')} />
