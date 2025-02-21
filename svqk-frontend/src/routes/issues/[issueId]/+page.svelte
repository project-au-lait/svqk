<script lang="ts">
  import { invalidateAll } from '$app/navigation';
  import IssueForm from '$lib/domain/issue/IssueForm.svelte';
  import Journals from '$lib/domain/issue/Journals.svelte';
  import { t } from '$lib/translations';
  import type { PageData } from './$types';

  let { data }: { data: PageData } = $props();
  let issue = $derived(data.issue);

  async function handleAfterSave(id?: number) {
    await invalidateAll();
  }
</script>

<IssueForm {issue} {handleAfterSave} actionBtnLabel={$t('msg.update')} />
<Journals journals={issue.journals} />
