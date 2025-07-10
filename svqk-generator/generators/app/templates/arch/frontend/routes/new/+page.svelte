<%_ include('../../../../lib/typescript-common'); -%>
<script lang="ts">
  import { goto } from '$app/navigation';
  <%- tscom.impDclF %>
  import <%= entityNmPascal %>Form from '$lib/domain/<%= entityNmPlural %>/<%= entityNmPascal %>Form.svelte';

  let <%= entityNmCamel %> = $state({<%= tscom.idObj %>} as <%= entityNmPascal %>Model);

  async function handleAfterSave(id?: <%= tscom.idType %>) {
    await goto(`/<%= entityNmPlural %>/<%= tscom.idPath %>`);
  }
</script>

<<%= entityNmPascal %>Form bind:<%= entityNmCamel %> {handleAfterSave} />
