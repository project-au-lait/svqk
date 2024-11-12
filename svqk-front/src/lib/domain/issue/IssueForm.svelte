<script lang="ts">
  import type { IdModel, IssueModel, IssueStatusModel, TrackerModel } from '$lib/arch/api/Api';
  import ApiHandler from '$lib/arch/api/ApiHandler';
  import FormValidator from '$lib/arch/form/FormValidator';
  import InputField from '$lib/arch/form/InputField.svelte';
  import SelectBox from '$lib/arch/form/SelectBox.svelte';
  import { messageStore } from '$lib/arch/global/MessageStore';
  import { t } from '$lib/translations';
  import * as yup from 'yup';
  import { issueStatuses, trackers } from './IssueMasterStore';

  interface Props {
    issue: IssueModel;
    handleAfterSave: (id?: number) => Promise<void>;
    actionBtnLabel: string;
  }

  let { issue = $bindable(), handleAfterSave, actionBtnLabel }: Props = $props();

  const spec = {
    subject: yup.string().required().label($t('msg.label.issue.subject'))
  };

  const form = FormValidator.createForm(spec, save);

  async function save() {
    const response = await ApiHandler.handle<IdModel>(fetch, (api) =>
      api.issues.issuesCreate(issue)
    );

    if (response) {
      await handleAfterSave(response.id);
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
  <div>
    <input id="save" type="submit" name="action" value={actionBtnLabel} />
  </div>
</form>
