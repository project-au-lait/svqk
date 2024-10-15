<script lang="ts">
  import type { IssueModel, IssueStatusModel, TrackerModel } from '$lib/arch/api/Api';
  import { pageStore } from '$lib/arch/global/PageStore';
  import IssueForm from '$lib/issue/IssueForm.svelte';
  import { t } from '$lib/translations';

  const issue = { issueStatus: {} as IssueStatusModel, tracker: {} as TrackerModel } as IssueModel;

  pageStore.setTitle($t('msg.newIssue'));
</script>

<IssueForm {issue} isNew={true} />
