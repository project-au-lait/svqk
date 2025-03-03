<script lang="ts">
  import { goto } from '$app/navigation';
  import type { IssueModel, IssueStatusModel, TrackerModel } from '$lib/arch/api/Api';
  import IssueForm from '$lib/domain/issue/IssueForm.svelte';
  import { t } from '$lib/translations';

  let issue = $state({
    issueStatus: {} as IssueStatusModel,
    tracker: {} as TrackerModel
  } as IssueModel);

  async function handleAfterSave(id?: number) {
    await goto(`/issues/${id}`);
  }
</script>

<IssueForm bind:issue {handleAfterSave} />
