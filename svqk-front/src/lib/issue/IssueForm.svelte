<script lang="ts">
  import { goto, invalidateAll } from '$app/navigation';
  import type { IdModel, IssueModel, IssueStatusModel, TrackerModel } from '$lib/arch/api/Api';
  import ApiHandler from '$lib/arch/api/ApiHandler';
  import FormValidator from '$lib/arch/form/FormValidator';
  import InputField from '$lib/arch/form/InputField.svelte';
  import SelectBox from '$lib/arch/form/SelectBox.svelte';
  import { messageStore } from '$lib/arch/global/MessageStore';
  import { t } from '$lib/translations';
  import { onMount } from 'svelte';
  import * as yup from 'yup';

  export let issue: IssueModel;
  export let isNew: boolean;

  let issueStatuses = [] as IssueStatusModel[];
  let trackers = [] as TrackerModel[];

  $: action = isNew ? $t('msg.register') : $t('msg.update');

  const spec = {
    subject: yup.string().required().label($t('msg.label.issue.subject'))
  };

  const form = FormValidator.createForm(spec, save);

  onMount(async () => {
    issueStatuses = (await ApiHandler.handle<IssueStatusModel[]>(fetch, (api) =>
      api.issueStatuses.issueStatusesList()
    ))!;

    trackers = (await ApiHandler.handle<TrackerModel[]>(fetch, (api) =>
      api.tracker.trackerList()
    ))!;
  });

  async function save() {
    const response = await ApiHandler.handle<IdModel>(fetch, (api) =>
      api.issues.issuesCreate(issue)
    );

    if (response) {
      if (isNew) {
        await goto(`/issues/${response.id}`);
      } else {
        await invalidateAll();
      }
      messageStore.show($t('msg.saved'));
    }
  }
</script>

<form use:form>
  <div>
    <InputField id="subject" label={$t('msg.subject')} bind:value={issue.subject} />
  </div>
  <div>
    <label for="description">{$t('msg.description')}</label>
    <textarea id="description" style="width:100%" bind:value={issue.description}></textarea>
  </div>
  <div class="grid">
    <div>
      {#if issueStatuses.length}
        <SelectBox
          id="status"
          label={$t('msg.status')}
          options={issueStatuses}
          bind:value={issue.issueStatus.id}
        />
      {/if}
    </div>
    <div>
      {#if trackers.length}
        <SelectBox
          id="tracker"
          label={$t('msg.tracker')}
          options={trackers}
          bind:value={issue.tracker.id}
        />
      {/if}
    </div>
    <div>
      <InputField id="dueDate" type="date" label={$t('msg.dueDate')} bind:value={issue.dueDate} />
    </div>
  </div>
  <div>
    <input id="save" type="submit" name="action" value={action} />
  </div>
</form>
