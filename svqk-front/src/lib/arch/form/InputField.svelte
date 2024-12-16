<script lang="ts">
  import { ValidationMessage } from '@felte/reporter-svelte';

  interface Props {
    id: string;
    label: string;
    type?: string;
    value: any;
  }

  let { id, label, type = 'text', value = $bindable() }: Props = $props();

  function setType(node: HTMLInputElement) {
    node.type = type;
  }
</script>

<label for={id}>{label}</label>
<ValidationMessage for={id} let:messages>
  <input {id} name={id} use:setType bind:value aria-describedby="invalid-{id}" />
  <small id="invalid-{id}">{messages || ''}</small>
</ValidationMessage>
