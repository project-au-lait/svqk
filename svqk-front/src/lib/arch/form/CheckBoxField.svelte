<script lang="ts">
  import { ValidationMessage } from '@felte/reporter-svelte';

  interface Props {
    id: string;
    label: string;
    checked?: boolean;
  }

  let { id, label, checked = $bindable() }: Props = $props();
</script>

<ValidationMessage for={id}>
  {#snippet children({ messages: message }: { messages: string[] | null })}
    <input {id} name={id} type="checkbox" bind:checked aria-describedby="invalid-{id}" />
    <label for={id}>{label}</label>
    <small id="invalid-{id}">{message || ''}</small>
  {/snippet}
</ValidationMessage>
