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
<ValidationMessage for={id} let:messages>
  <div>
    <textarea {id} name={id} bind:value aria-describedby="invalid-{id}"></textarea>
  </div>
  <small id="invalid-{id}">{messages || ''}</small>
</ValidationMessage>
