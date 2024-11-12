<script lang="ts">
  import type { PageData } from './$types';

  // <.>
  let { data }: { data: PageData } = $props();
  const hello = data.hello;
</script>

<!--.-->
<h1 id="message">{hello.message}</h1>
