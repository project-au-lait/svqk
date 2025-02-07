<script lang="ts">
  import { afterNavigate, goto } from '$app/navigation';
  import type { IssueModel } from '$lib/arch/api/Api';
  import CheckBox from '$lib/arch/form/CheckBox.svelte';
  import FormValidator from '$lib/arch/form/FormValidator';
  import InputField from '$lib/arch/form/InputField.svelte';
  import SelectBox from '$lib/arch/form/SelectBox.svelte';
  import ListTable, { ColumnsBuilder } from '$lib/arch/search/ListTable.svelte';
  import DateUtils from '$lib/arch/util/DateUtils';
  import { issueStatuses } from '$lib/domain/issue/IssueStatusMasterStore';
  import { t } from '$lib/translations';
  import type { PageData } from '../issues/$types';

  let { data }: { data: PageData } = $props();
  let { condition } = $state(data);
  let { result } = $derived(data);

  let open = $state(false);

  const form = FormValidator.createForm({}, search); // <.>

  const columns = new ColumnsBuilder<IssueModel>()
    .add('#', 'i.id', () => issueIdAnchor)
    .add($t('msg.tracker'), 'i.tracker', (issue) => issue.tracker.name)
    .add($t('msg.status'), 'i.issueStatus', (issue) => issue.issueStatus.name)
    .add($t('msg.subject'), 'i.subject', (issue) => issue.subject, ['align-left'])
    .add($t('msg.dueDate'), 'i.dueDate', (issue) => DateUtils.date(issue.dueDate))
    .add($t('msg.updatedAt'), 'i.updatedAt', (issue) => DateUtils.datetime(issue.updatedAt))
    .build(); // <.>

  function search() {
    goto(`?q=${encodeURIComponent(JSON.stringify(condition))}`);
  }

  afterNavigate(() => {
    condition = data.condition;
    open = Boolean(condition.subjectOnly || condition.issueStatuses?.length || condition.dueDate);
  });
</script>

<section>
  <form use:form>
    <fieldset role="search">
      <input type="search" bind:value={condition.text} />
      <input type="submit" value="Search" />
    </fieldset>

    <details {open}>
      <summary style="display: flex; justify-content: end;">{$t('msg.advancedSearch')}</summary>
      <div class="grid">
        <div>
          <label for="option">{$t('msg.option')}</label>
          <CheckBox
            id="subject-only"
            label={$t('msg.searchBySubjectOnly')}
            bind:checked={condition.subjectOnly}
          />
        </div>

        <div>
          <SelectBox
            id="status"
            label={$t('msg.multipleStatuses')}
            options={$issueStatuses}
            multiple={true}
            bind:value={condition.issueStatuses}
          />
        </div>

        <div>
          <InputField
            id="due-date"
            label={$t('msg.dueDate')}
            type="date"
            bind:value={condition.dueDate}
          />
        </div>
      </div>
    </details>
  </form>
</section>

<section>
  <a id="newIssue" href="/issues/new"> {$t('msg.newIssue')} </a>
</section>

<section>
  <ListTable {result} {columns} bind:pageControl={condition.pageControl} {search} />
</section>

<!-- for ListTable issueId Column -->
{#snippet issueIdAnchor(issue: IssueModel)}
  <a href={`/issues/${issue.id}`}>{issue.id}</a>
{/snippet}
