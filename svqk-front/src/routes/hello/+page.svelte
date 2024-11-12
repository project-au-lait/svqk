<script lang="ts">
  import type { PageData } from './$types';

  
  interface Props {
    // <.>
    data: PageData;
  }

  let { data }: Props = $props();
  const hello = data.hello;
</script>

<!--.-->
<h1 id="message">{hello.message}</h1>
