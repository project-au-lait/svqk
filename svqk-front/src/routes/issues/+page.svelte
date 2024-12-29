<script lang="ts">
  import type { IssueModel, IssueSearchResultModel } from '$lib/arch/api/Api';
  import ApiHandler from '$lib/arch/api/ApiHandler';
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
  let { result, condition } = $state(data);

  const form = FormValidator.createForm({}, search); // <.>

  const columns = new ColumnsBuilder<IssueModel>()
    .add('#', 'id', () => issueIdAnchor)
    .add($t('msg.tracker'), 'tracker', (issue) => issue.tracker.name)
    .add($t('msg.status'), 'issueStatus', (issue) => issue.issueStatus.name)
    .add($t('msg.subject'), 'subject', (issue) => issue.subject, ['align-left'])
    .add($t('msg.dueDate'), 'dueDate', (issue) => DateUtils.date(issue.dueDate))
    .add($t('msg.updatedAt'), 'updatedAt', (issue) => DateUtils.datetime(issue.updatedAt))
    .build(); // <.>

  // <.>
  async function search() {
    // <.>
    const r = await ApiHandler.handle<IssueSearchResultModel>(fetch, (api) =>
      api.issues.issuesSearch(condition)
    );

    // <.>
    if (r) {
      result = r;
    }
  }
</script>

<section>
  <form use:form>
    <fieldset role="search">
      <input type="search" bind:value={condition.text} />
      <input type="submit" value="Search" />
    </fieldset>

    <details>
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
  <ListTable {result} {columns} bind:condition {search} />
</section>

<!-- for ListTable issueId Column -->
{#snippet issueIdAnchor(issue: IssueModel)}
  <a href={`/issues/${issue.id}`}>{issue.id}</a>
{/snippet}
