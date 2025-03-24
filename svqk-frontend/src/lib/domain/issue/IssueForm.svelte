<script lang="ts">
  import type { IssueModel, JournalModel } from '$lib/arch/api/Api';
  import ApiHandler from '$lib/arch/api/ApiHandler';
  import FormValidator from '$lib/arch/form/FormValidator';
  import InputField from '$lib/arch/form/InputField.svelte';
  import SelectBox from '$lib/arch/form/SelectBox.svelte';
  import TextArea from '$lib/arch/form/TextArea.svelte';
  import { messageStore } from '$lib/arch/global/MessageStore';
  import { issueStatuses } from '$lib/domain/issue/IssueStatusMasterStore';
  import { trackers } from '$lib/domain/issue/TrackerMasterStore';
  import { t } from '$lib/translations';
  import * as yup from 'yup';
  import { goto } from '$app/navigation';

  interface Props {
    issue: IssueModel;
    updateMode?: boolean;
    handleAfterSave: (id?: number) => Promise<void>;
    handleAfterDelete?: (id?: number) => Promise<void>;
  }

  let {
    issue = $bindable(),
    updateMode = false,
    handleAfterSave,
    handleAfterDelete = async (id) => {}
  }: Props = $props();
  let journal = $state({ issueId: issue.id } as JournalModel);

  const spec = {
    subject: yup.string().required().label($t('msg.label.issue.subject'))
  };

  const form = FormValidator.createForm(spec, save, del);

  async function save() {
    const response = await ApiHandler.handle<number>(fetch, (api) =>
      updateMode
        ? api.issues.issuesUpdate(issue.id, { issue, journal })
        : api.issues.issuesCreate(issue)
    );

    if (response) {
      journal.notes = '';
      await handleAfterSave(response);
      messageStore.show($t('msg.saved'));
    }
  }

  async function del() {
    const response = await ApiHandler.handle<number>(fetch, (api) =>
      api.issues.issuesDelete(issue.id, issue)
    );

    if (response) {
      await handleAfterDelete();
      messageStore.show($t('msg.deleted'));
    }
  }
</script>

<form use:form>
  <div>
    <InputField id="subject" label={$t('msg.subject')} bind:value={issue.subject} />
  </div>
  <div>
    <TextArea id="description" label={$t('msg.description')} bind:value={issue.description} />
  </div>
  <div class="grid">
    <div>
      <SelectBox
        id="status"
        label={$t('msg.status')}
        options={$issueStatuses}
        bind:value={issue.issueStatus.id}
      />
    </div>
    <div>
      <SelectBox
        id="tracker"
        label={$t('msg.tracker')}
        options={$trackers}
        bind:value={issue.tracker.id}
      />
    </div>
    <div>
      <InputField id="dueDate" type="date" label={$t('msg.dueDate')} bind:value={issue.dueDate} />
    </div>
  </div>
  {#if updateMode}
    <div>
      <TextArea id="notes" label={$t('msg.notes')} bind:value={journal.notes} />
    </div>
  {/if}
  <div class="grid">
    <div>
      <button type="submit" id="save" data-handler={save.name}
        >{updateMode ? $t('msg.update') : $t('msg.register')}</button
      >
    </div>
    {#if updateMode}
      <div>
        <button type="submit" id="del" data-handler={del.name}>{$t('msg.delete')}</button>
      </div>
    {/if}
  </div>
</form>
