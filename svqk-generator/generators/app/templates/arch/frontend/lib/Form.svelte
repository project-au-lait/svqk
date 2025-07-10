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
  import * as m from '$lib/paraglide/messages';
  import { string, date, boolean, number } from 'yup';

  interface Props {
    <%= entityNmCamel %>: <%= entityNmPascal %>Model;
    updateMode?: boolean;
    handleAfterSave: (id?: <%= tscom.idType %>) => Promise<void>;
    handleAfterDelete?: () => Promise<void>;
  }

  let {
    <%= entityNmCamel %> = $bindable(),
     updateMode = false,
     handleAfterSave,
     handleAfterDelete = async (id) => {}
  }: Props = $props();

  const spec = {
    <%_ for (field of nonIdFields) { _%>
      <%_ if (field.required) { _%>
        <%= field.fieldName %>: <%= tscom.dataType(field.javaType) %>().required().label(m.label_<%= entityNmCamel %>_<%= field.fieldName %>),
      <%_ } _%>
    <%_ } _%>
  };

  const form = FormValidator.createForm(spec, save, del);

  async function save() {
    const response = await ApiHandler.handle<<%= tscom.idType %>>(fetch, (api) => 
      <%- tscom.buildSaveApiCall(entityNmCamel, compIdFields) %>
    );

    if (response) {
      await handleAfterSave(response);
      messageStore.show(m.saved());
    }
  }

  async function del() {
    const response = await ApiHandler.handle<<%= tscom.idType %>>(fetch, (api) =>
      <%- tscom.buildDeleteApiCall(entityNmCamel, compIdFields) %>
    );

    if (response) {
      await handleAfterDelete();
      messageStore.show(m.deleted());
    }
  }
</script>

<form use:form>
  <%_ for (field of compIdFields || [idField]) { _%>
  <div>
    <%- tscom.inputField(field, !!compIdFields, 'updateMode') %>
  </div>
  <%_ } _%>
  <%_ for (field of nonIdFields) { _%>
  <div>
    <%- tscom.inputField(field, false) %>
  </div>
  <%_ } _%>
  <div class="grid">
    <div>
      <button type="submit" id="save" data-handler={save.name}>
        {updateMode ? m.update() : m.register()}
      </button>
    </div>
  {#if updateMode}
    <div>
      <button type="submit" id="del" data-handler={del.name}>
        {m.delete()}
      </button>
    </div>
  {/if}
  </div>
</form>