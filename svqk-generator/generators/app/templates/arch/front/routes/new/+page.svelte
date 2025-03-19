<%_ include('../../../../lib/typescript-common'); -%>
<script lang="ts">
  import { goto } from '$app/navigation';
  import type { <%= entityNmPascal %>Model<%= importingIdType %> } from '$lib/arch/api/Api';
  import <%= entityNmPascal %>Form from '$lib/domain/<%= entityNmPlural %>/<%= entityNmPascal %>Form.svelte';
  import { t } from '$lib/translations';

  let <%= entityNmCamel %> = $state({<%= idObj%>} as <%= entityNmPascal %>Model);

  async function handleAfterSave(id?: <%= idType %>) {
    await goto(`/<%= entityNmPlural %>/<%= idPath %>`);
  }
</script>

<<%= entityNmPascal %>Form bind:<%= entityNmCamel %> {handleAfterSave} actionBtnLabel={$t('msg.register')} />
