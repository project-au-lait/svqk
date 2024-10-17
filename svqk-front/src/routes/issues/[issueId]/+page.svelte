<script lang="ts">
  import { invalidateAll } from '$app/navigation';
  import { pageStore } from '$lib/arch/global/PageStore';
  import IssueForm from '$lib/issue/IssueForm.svelte';
  import type { PageData } from './$types';

  export let data: PageData;

  $: ({ issue } = data);

  $: pageStore.setTitle(issue.subject);

  const handleAfterSave = async () => {
    await invalidateAll();
  };
</script>

<IssueForm {issue} {handleAfterSave} />
