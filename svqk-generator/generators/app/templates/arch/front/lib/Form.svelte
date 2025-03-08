<%_ include('../../../lib/frontend-common', { entityNmPascal, compIdFields }); -%>
<script lang="ts">
  import type { <%= entityNmPascal %>Model<%= importingIdType %>} from '$lib/arch/api/Api';
  import ApiHandler from '$lib/arch/api/ApiHandler';
  import FormValidator from '$lib/arch/form/FormValidator';
  import InputField from '$lib/arch/form/InputField.svelte';
  import { messageStore } from '$lib/arch/global/MessageStore';
  import { t } from '$lib/translations';
  import { string } from 'yup';

  interface Props {
    <%= entityNmCamel %>: <%= entityNmPascal %>Model;
    handleAfterSave: (id?: <%= idType %>) => Promise<void>;
    actionBtnLabel: string;
  }

  let { <%= entityNmCamel %> = $bindable(), handleAfterSave, actionBtnLabel }: Props = $props();

  const spec = {
    <%_ for (field of fields) { _%>
      <%_ if (field.required) { _%>
        <%= field.fieldName %>: string().required().label($t('msg.label.<%= entityNmCamel %>.<%= field.fieldName %>')),
      <%_ } _%>
    <%_ } _%>
  };

  const form = FormValidator.createForm(spec, save);

  async function save() {
    const response = await ApiHandler.handle<<%= idType %>>(fetch, (api) => 
      <%= entityNmCamel %>.id ? api.<%= entityNmCamel %>.<%= entityNmCamel %>Update(<%= entityNmCamel %>) : api.<%= entityNmCamel %>.<%= entityNmCamel %>Create(<%= entityNmCamel %>));

    if (response) {
      await handleAfterSave(response);
      messageStore.show($t('msg.saved'));
    }
  }
</script>

<form use:form>
  <%_ for (field of fields) { _%>
    <%_ if (compIdFields && field.id) { _%>
      <%_ for (compIdField of compIdFields) { _%>
    <div>
      <InputField id="<%= compIdField.fieldName %>" label={$t(`msg.label.<%= entityNmCamel %>.<%= compIdField.fieldName %>`)} bind:value={<%= entityNmCamel %>.id.<%= compIdField.fieldName %>} />
    </div>
      <%_ } _%>
    <%_ } else { _%>
    <div>
      <InputField id="<%= field.fieldName %>" label={$t(`msg.label.<%= entityNmCamel %>.<%= field.fieldName %>`)} bind:value={<%= entityNmCamel %>.<%= field.fieldName %>} />
    </div>
    <%_ } _%>
  <%_ } _%>
  <div>
    <button id="save" type="submit">{actionBtnLabel}</button>
  </div>
</form>
