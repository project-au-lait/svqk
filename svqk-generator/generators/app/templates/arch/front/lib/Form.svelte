<%_ include('../../../lib/frontend-common', { entityNmPascal, compIdFields }); -%>
<script lang="ts">
  import type { <%= entityNmPascal %>Model<%= importingIdType %>} from '$lib/arch/api/Api';
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
    handleAfterSave: (id?: <%= idType %>) => Promise<void>;
    handleAfterDelete: (id?: <%= idType %>) => Promise<void>;
  }

  let { <%= entityNmCamel %> = $bindable(), handleAfterSave, handleAfterDelete }: Props = $props();
  let isUpdate = typeof <%= entityNmCamel %>.id != null;

  <%_ 
    const dataType = (javaType) => {
      const typeMap = {
        'Integer': 'number',
        'String': 'string',
        'java.time.LocalDate': 'date',
        'java.time.LocalDateTime': 'date',
        'Boolean': 'boolean'
      };
      return typeMap[javaType] || 'string'
    }
  %>

  const spec = {
    <%_ for (field of fields) { _%>
      <%_ if (field.required) { _%>
        <%= field.fieldName %>: <%= dataType(field.javaType) %>().required().label($t('msg.label.<%= entityNmCamel %>.<%= field.fieldName %>')),
      <%_ } _%>
    <%_ } _%>
  };

  const form = FormValidator.createForm(spec, save, delete<%= entityNmPascal %>);

  async function save() {
    const response = await ApiHandler.handle<<%= idType %>>(fetch, (api) => 
      isUpdate ? api.<%= entityNmCamel %>.<%= entityNmCamel %>Update(<%= entityNmCamel %>) : api.<%= entityNmCamel %>.<%= entityNmCamel %>Create(<%= entityNmCamel %>));

    if (response) {
      await handleAfterSave(response);
      messageStore.show($t('msg.saved'));
    }
  }

  async function delete<%= entityNmPascal %>() {
    const response = await ApiHandler.handle<<%= idType %>>(fetch, (api) =>
      <%_ if (compIdFields) { _%>
        api.<%= entityNmCamel %>.<%= entityNmCamel %>Delete(<%_ for (compIdField of compIdFields) { _%><%= entityNmCamel %>.id.<%= compIdField.fieldName %>,<%_ } _%><%= entityNmCamel %>)
      <%_ } else { _%>
        api.<%= entityNmCamel %>.<%= entityNmCamel %>Delete(<%= entityNmCamel %>.id, <%= entityNmCamel %>)
      <%_ } _%>        
    );

    if (response) {
      await handleAfterDelete();
      messageStore.show($t('msg.deleted'));
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
        <%_ if (field.javaType === 'Integer') { _%>
          <InputField type='number' id="<%= field.fieldName %>" label={$t(`msg.label.<%= entityNmCamel %>.<%= field.fieldName %>`)} bind:value={<%= entityNmCamel %>.<%= field.fieldName %>} />
        <%_ } else if (field.javaType === 'String' && field.stringLength <= 128) { _%>
          <InputField id="<%= field.fieldName %>" label={$t(`msg.label.<%= entityNmCamel %>.<%= field.fieldName %>`)} bind:value={<%= entityNmCamel %>.<%= field.fieldName %>} />
        <%_ } else if (field.javaType === 'String') { _%>
          <TextArea id="<%= field.fieldName %>" label={$t(`msg.label.<%= entityNmCamel %>.<%= field.fieldName %>`)} bind:value={<%= entityNmCamel %>.<%= field.fieldName %>} />
        <%_ } else if (field.javaType === 'java.time.LocalDate') { _%>
          <InputField type="date" id="<%= field.fieldName %>" label={$t(`msg.label.<%= entityNmCamel %>.<%= field.fieldName %>`)} bind:value={<%= entityNmCamel %>.<%= field.fieldName %>} />
        <%_ } else if (field.javaType === 'java.time.LocalDateTime') { _%>
          <InputField type="datetime-local" id="<%= field.fieldName %>" label={$t(`msg.label.<%= entityNmCamel %>.<%= field.fieldName %>`)} bind:value={<%= entityNmCamel %>.<%= field.fieldName %>} />
        <%_ } else if (field.javaType === 'Boolean') { _%>
          <CheckBox id="<%= field.fieldName %>" label={$t(`msg.label.<%= entityNmCamel %>.<%= field.fieldName %>`)} bind:checked={<%= entityNmCamel %>.<%= field.fieldName %>} />
        <%_ } else { _%>
          <SelectBox id="<%= field.fieldName %>" label={$t(`msg.label.<%= entityNmCamel %>.<%= field.fieldName %>`)} bind:value={<%= entityNmCamel %>.<%= field.fieldName %>} options={[]} <%= field.multiple ? 'multiple' : '' %> />
        <%_ } _%>
      </div>
    <%_ } _%>
  <%_ } _%>
  <div>
    <button type="submit" id="save" data-handler={save.name}
      >{isUpdate ? $t('msg.update') : $t('msg.register')}</button
    >
    {#if isUpdate}
      <button type="submit" id="delete<%= entityNmPascal %>" data-handler={delete<%= entityNmPascal %>.name}
        >{$t('msg.delete')}</button
      >
    {/if}
  </div>
</form>
