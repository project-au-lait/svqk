<script lang="ts">
  import { goto, invalidateAll } from '$app/navigation';
  import { messageStore } from '$lib/arch/global/MessageStore';
  import type { PageData } from './$types';
  import * as yup from 'yup';
  import ApiHandler from '$lib/arch/api/ApiHandler';
  import type { IdModel } from '$lib/arch/api/Api';
  import InputField from '$lib/arch/form/InputField.svelte';
  import FormValidator from '$lib/arch/form/FormValidator';
  import SelectBox from '$lib/arch/form/SelectBox.svelte';
  import { pageStore } from '$lib/arch/global/PageStore';
  import { t } from '$lib/translations';

  export let data: PageData;

  let { issue, issueStatuses, trackers, isNew } = data;
  $: ({ issue, issueStatuses, trackers, isNew } = data);

  $: {
    const subject = isNew ? $t('msg.newIssue') : issue.subject;
    pageStore.setTitle(subject);
  }

  $: action = isNew ? $t('msg.register') : $t('msg.update');

  const spec = {
    subject: yup.string().required().label($t('msg.label.issue.subject'))
  };

  const form = FormValidator.createForm(spec, save);

  async function save() {
    const response = await ApiHandler.handle<IdModel>(fetch, (api) =>
      api.issues.issuesCreate(issue)
    );

    if (response) {
      // messageStore.show(`${action}しました。`);
      messageStore.show($t('msg.saved'));
      if (isNew) {
        await goto(`/issues/${response.id}`);
      } else {
        await invalidateAll();
      }
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
        options={issueStatuses}
        bind:value={issue.issueStatus.id}
      />
    </div>
    <div>
      <SelectBox
        id="tracker"
        label={$t('msg.tracker')}
        options={trackers}
        bind:value={issue.tracker.id}
      />
    </div>
    <div>
      <InputField id="dueDate" type="date" label={$t('msg.dueDate')} bind:value={issue.dueDate} />
    </div>
  </div>
  <div>
    <button id="save" type="submit">{action}</button>
  </div>
</form>
