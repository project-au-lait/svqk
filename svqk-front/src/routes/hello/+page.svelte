<script lang="ts">
  import type { PageData } from './$types';

  // <.>
  export let data: PageData;
  const hello = data.hello;
</script>

<!--.-->
<h1 id="message">{hello.message}</h1>
