<script lang="ts">
  import type { PageProps } from './$types';

  // <.>
  let { data }: PageProps = $props();
  const hello = data.hello;
</script>

<!--.-->
<h1 id="message">{hello.message}</h1>
