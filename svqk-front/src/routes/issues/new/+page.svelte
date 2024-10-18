<script lang="ts">
  import { goto } from '$app/navigation';
  import type { IssueModel, IssueStatusModel, TrackerModel } from '$lib/arch/api/Api';
  import { pageStore } from '$lib/arch/global/PageStore';
  import IssueForm from '$lib/domain/issue/IssueForm.svelte';
  import { t } from '$lib/translations';

  const issue = { issueStatus: {} as IssueStatusModel, tracker: {} as TrackerModel } as IssueModel;

  pageStore.setTitle($t('msg.newIssue'));

  async function handleAfterSave(id?: number) {
    await goto(`/issues/${id}`);
  }
</script>

<IssueForm {issue} {handleAfterSave} actionBtnLabel={$t('msg.register')} />
