<script lang="ts">
  import { goto } from '$app/navigation';
  import { invalidateAll } from '$app/navigation';
  import IssueForm from '$lib/domain/issue/IssueForm.svelte';
  import Journals from '$lib/domain/issue/Journals.svelte';
  import type { PageProps } from './$types';

  let { data }: PageProps = $props();
  let issue = $state(data.issue);
  $effect(() => {
    issue = data.issue;
  });

  async function handleAfterSave(id?: number) {
    await invalidateAll();
  }

  async function handleAfterDelete() {
    await goto(`/issues/`);
  }
</script>

<IssueForm bind:issue updateMode={true} {handleAfterSave} {handleAfterDelete} />
<Journals journals={issue.journals} />
