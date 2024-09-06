<script lang="ts">
  import { ValidationMessage } from '@felte/reporter-svelte';

  export let id: string;
  export let label: string;
  export let type = 'text';
  export let value: any;

  function setType(node: HTMLInputElement) {
    node.type = type;
  }
</script>

<label for={id}>{label}</label>
<ValidationMessage for={id} let:messages={message}>
  <input {id} name={id} use:setType bind:value aria-describedby="invalid-{id}" />
  <small id="invalid-{id}">{message || ''}</small>
</ValidationMessage>
