<script lang="ts">
  import { run } from 'svelte/legacy';

  import { invalidateAll } from '$app/navigation';
  import { pageStore } from '$lib/arch/global/PageStore';
  import IssueForm from '$lib/domain/issue/IssueForm.svelte';
  import type { PageData } from './$types';
  import { t } from '$lib/translations';

  interface Props {
    data: PageData;
  }

  let { data }: Props = $props();

  let issue = $derived(data.issue);

  run(() => {
    pageStore.setTitle(issue.subject);
  });

  async function handleAfterSave(id?: number) {
    await invalidateAll();
  }
</script>

<IssueForm {issue} {handleAfterSave} actionBtnLabel={$t('msg.update')} />
