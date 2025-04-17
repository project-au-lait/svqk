<script lang="ts">
  import { ValidationMessage } from '@felte/reporter-svelte';

  interface Props {
    id: string;
    label: string;
    checked?: boolean;
    disabled?: boolean;
  }

  let { id, label, checked = $bindable(), disabled = false }: Props = $props();
</script>

<ValidationMessage for={id} let:messages>
  <input {id} name={id} type="checkbox" bind:checked aria-describedby="invalid-{id}" {disabled} />
  <label for={id}>{label}</label>
  <small id="invalid-{id}">{messages || ''}</small>
</ValidationMessage>
