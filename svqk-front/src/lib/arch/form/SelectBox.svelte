<script lang="ts">
  interface Props {
    id: string;
    label: string;
    value: any;
    options: { id: any; name: string }[];
    multiple?: boolean;
    size?: number;
  }

  let {
    id,
    label,
    value = $bindable(),
    options,
    multiple = false,
    size = 3
  }: Props = $props();

  let stateOptions = $state(options);
</script>

<label for={id}>{label}</label>

{#if stateOptions.length}
  {#if multiple}
    <select {id} multiple {size} bind:value>
      {#each stateOptions as option}
        <option value={option}>{option.name}</option>
      {/each}
    </select>
  {:else}
    <select {id} bind:value>
      {#each stateOptions as option}
        <option value={option.id}>{option.name}</option>
      {/each}
    </select>
  {/if}
{/if}
