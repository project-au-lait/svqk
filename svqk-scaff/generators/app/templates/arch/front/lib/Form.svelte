<script lang="ts">
  import type { <%= entityNmPascal %>Model } from '$lib/arch/api/Api';
  import ApiHandler from '$lib/arch/api/ApiHandler';
  import FormValidator from '$lib/arch/form/FormValidator';
  import InputField from '$lib/arch/form/InputField.svelte';
  import { messageStore } from '$lib/arch/global/MessageStore';
  import { t } from '$lib/translations';
  import * as yup from 'yup';

  interface Props {
    <%= entityNmCamel %>: <%= entityNmPascal %>Model;
    handleAfterSave: (id?: number) => Promise<void>;
    actionBtnLabel: string;
  }

  let { <%= entityNmCamel %> = $bindable(), handleAfterSave, actionBtnLabel }: Props = $props();

  const spec = {
    <% for (field of fields) { %>
      <% if (field.required) { %>
        <%= field.fieldName %>: yup.string().required().label($t('msg.label.<%= entityNmCamel %>.<%= field.fieldName %>')),
      <% } %>
    <% } %>
  };

  const form = FormValidator.createForm(spec, save);

  async function save() {
    const response = await ApiHandler.handle<number>(fetch, (api) => 
      api.<%= entityNmCamel %>.<%= entityNmCamel %>Create(<%= entityNmCamel %>));

    if (response) {
      await handleAfterSave(response);
      messageStore.show($t('msg.saved'));
    }
  }
</script>

<form use:form>
  <% for (field of fields) { %>
    <div>
      <InputField id="<%= field.fieldName %>" label={$t(`msg.label.<%= entityNmCamel %>.<%= field.fieldName %>`)} bind:value={<%= entityNmCamel %>.<%= field.fieldName %>} />
    </div>
  <% } %>
  <div>
    <button id="save" type="submit">{$t('msg.register')}</button>
  </div>
</form>
