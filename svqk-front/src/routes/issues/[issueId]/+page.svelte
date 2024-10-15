<script lang="ts">
  import { pageStore } from '$lib/arch/global/PageStore';
  import IssueForm from '$lib/issue/IssueForm.svelte';
  import type { PageData } from './$types';

  export let data: PageData;

  $: ({ issue } = data);

  $: pageStore.setTitle(issue.subject);
</script>

<IssueForm {issue} isNew={false} />
