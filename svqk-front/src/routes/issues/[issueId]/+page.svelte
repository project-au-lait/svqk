<script lang="ts">
  import { invalidateAll } from '$app/navigation';
  import IssueForm from '$lib/domain/issue/IssueForm.svelte';
  import type { PageData } from './$types';
  import { t } from '$lib/translations';

  let { data }: { data: PageData } = $props();
  let issue = $derived(data.issue);

  async function handleAfterSave(id?: number) {
    await invalidateAll();
  }
</script>

<IssueForm {issue} {handleAfterSave} actionBtnLabel={$t('msg.update')} />
