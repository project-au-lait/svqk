<%_ include('../../../lib/typescript-common'); -%>
<script lang="ts">
  <%- tscom.impDclF %>
  import ApiHandler from '$lib/arch/api/ApiHandler';
  import FormValidator from '$lib/arch/form/FormValidator';
  import InputField from '$lib/arch/form/InputField.svelte';
  import TextArea from '$lib/arch/form/TextArea.svelte';
  import SelectBox from '$lib/arch/form/SelectBox.svelte';
  import CheckBox from '$lib/arch/form/CheckBox.svelte';
  import { messageStore } from '$lib/arch/global/MessageStore';
  import { t } from '$lib/translations';
  import { string, date, boolean, number } from 'yup';

  interface Props {
    <%= entityNmCamel %>: <%= entityNmPascal %>Model;
    handleAfterSave: (id?: <%= tscom.idType %>) => Promise<void>;
    actionBtnLabel: string;
  }

  let { <%= entityNmCamel %> = $bindable(), handleAfterSave, actionBtnLabel }: Props = $props();

  const spec = {
    <%_ for (field of fields) { _%>
      <%_ if (field.required) { _%>
        <%= field.fieldName %>: <%= tscom.dataType(field.javaType) %>().required().label($t('msg.label.<%= entityNmCamel %>.<%= field.fieldName %>')),
      <%_ } _%>
    <%_ } _%>
  };

  const form = FormValidator.createForm(spec, save);

  async function save() {
    const response = await ApiHandler.handle<<%= tscom.idType %>>(fetch, (api) => 
      <%= entityNmCamel %>.id ? api.<%= entityNmCamel %>.<%= entityNmCamel %>Update(<%= entityNmCamel %>) : api.<%= entityNmCamel %>.<%= entityNmCamel %>Create(<%= entityNmCamel %>));

    if (response) {
      await handleAfterSave(response);
      messageStore.show($t('msg.saved'));
    }
  }
</script>

<form use:form>
  <%_ if(compIdFields) { _%>
    <%_ for (field of compIdFields || []) { _%>
  <div>
    <%- tscom.inputField(field, true) %>
  </div>
    <%_ } _%>  
    <%_ for (field of nonIdFields) { _%>
  <div>
    <%- tscom.inputField(field, false) %>
  </div>
    <%_ } _%>
  <%_ } else { _%>
    <%_ for (field of fields) { _%>
  <div>
    <%- tscom.inputField(field, false) %>
  </div>
    <%_ } _%>
  <%_ } _%>
  <div>
    <button id="save" type="submit">{actionBtnLabel}</button>
  </div>
</form>