<script lang="ts">
  interface Props {
    id: string;
    label: string;
    value: any;
  }

  let { id, label, value }: Props = $props();
</script>

<label for={id}>{label}</label>
<div id={id}>{value}</div>

