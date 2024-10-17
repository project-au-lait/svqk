<script lang="ts">
  export let id: string;
  export let label: string;
  export let value: any;
  export let options: { id: any; name: string }[];
  export let multiple = false;
  export let size = 3;
</script>

<label for={id}>{label}</label>

{#if options.length}
  {#if multiple}
    <select {id} multiple {size} bind:value>
      {#each options as option}
        <option value={option}>{option.name}</option>
      {/each}
    </select>
  {:else}
    <select {id} bind:value>
      {#each options as option}
        <option value={option.id}>{option.name}</option>
      {/each}
    </select>
  {/if}
{/if}
