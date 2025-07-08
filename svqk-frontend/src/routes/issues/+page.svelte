<script lang="ts">
  import { goto } from '$app/navigation';
  import type { IssueModel } from '$lib/arch/api/Api';
  import CheckBox from '$lib/arch/form/CheckBox.svelte';
  import FormValidator from '$lib/arch/form/FormValidator';
  import InputField from '$lib/arch/form/InputField.svelte';
  import SelectBox from '$lib/arch/form/SelectBox.svelte';
  import CriteriaUtils from '$lib/arch/search/CriteriaUtils';
  import ListTable, { ColumnsBuilder } from '$lib/arch/search/ListTable.svelte';
  import DateUtils from '$lib/arch/util/DateUtils';
  import { issueStatuses } from '$lib/domain/issue/IssueStatusMasterStore';
  import * as m from '$lib/paraglide/messages';
  import type { PageProps } from '../issues/$types';

  let { data }: PageProps = $props();
  let { criteria, open } = $state(data);
  let { result } = $derived(data);

  const form = FormValidator.createForm({}, search); // <.>

  const columns = new ColumnsBuilder<IssueModel>()
    .add('#', 'i.id', () => issueIdAnchor)
    .add(m.tracker(), 'i.tracker', (issue) => issue.tracker.name)
    .add(m.status(), 'i.issueStatus', (issue) => issue.issueStatus.name)
    .add(m.subject(), 'i.subject', (issue) => issue.subject, ['align-left'])
    .add(m.dueDate(), 'i.dueDate', (issue) => DateUtils.date(issue.dueDate))
    .add(m.updatedAt(), 'i.updatedAt', (issue) => DateUtils.datetime(issue.updatedAt))
    .build(); // <.>

  // <.>
  function search() {
    goto(CriteriaUtils.encode(criteria, { open }));
  }
</script>

<section>
  <form use:form>
    <fieldset role="search">
      <input id="search" type="search" bind:value={criteria.text} />
      <input type="submit" value="Search" />
    </fieldset>

    <details {open}>
      <summary style="display: flex; justify-content: end;">{m.advancedSearch()}</summary>
      <div class="grid">
        <div>
          <label for="subject-only">{m.option()}</label>
          <CheckBox
            id="subject-only"
            label={m.searchBySubjectOnly()}
            bind:checked={criteria.subjectOnly}
          />
        </div>

        <div>
          <SelectBox
            id="status"
            label={m.multipleStatuses()}
            options={$issueStatuses}
            multiple={true}
            bind:value={criteria.issueStatuses}
          />
        </div>

        <div>
          <InputField
            id="due-date"
            label={m.dueDate()}
            type="date"
            bind:value={criteria.dueDate}
          />
        </div>
      </div>
    </details>
  </form>
</section>

<section>
  <a id="newIssue" href="/issues/new"> {m.newIssue()} </a>
</section>

<section>
  <ListTable
    {result}
    {columns}
    bind:pageControl={criteria.pageControl}
    bind:sortOrders={criteria.sortOrders}
    {search}
  />
</section>

<!-- for ListTable issueId Column -->
{#snippet issueIdAnchor(issue: IssueModel)}
  <a href={`/issues/${issue.id}`}>{issue.id}</a>
{/snippet}
