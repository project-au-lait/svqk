<script lang="ts">
  import { ValidationMessage } from '@felte/reporter-svelte';

  interface Props {
    id: string;
    label: string;
    value: any;
    disabled?: boolean;
  }

  let { id, label, value = $bindable(), disabled = false }: Props = $props();
</script>

<label for={id}>{label}</label>
<ValidationMessage for={id} let:messages>
  <div>
    <textarea {id} name={id} bind:value aria-describedby="invalid-{id}" {disabled}></textarea>
  </div>
  <small id="invalid-{id}">{messages || ''}</small>
</ValidationMessage>
