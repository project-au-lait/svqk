<script lang="ts">
  import { ValidationMessage } from '@felte/reporter-svelte';

  interface Props {
    id: string;
    label: string;
    value: any;
  }

  let { id, label, value = $bindable() }: Props = $props();
</script>

<label for={id}>{label}</label>
<ValidationMessage for={id}>
  {#snippet children({ messages: message }: { messages: string[] | null })}
    <div>
      <textarea {id} name={id} bind:value aria-describedby="invalid-{id}"></textarea>
    </div>
    <small id="invalid-{id}">{message || ''}</small>
  {/snippet}
</ValidationMessage>
