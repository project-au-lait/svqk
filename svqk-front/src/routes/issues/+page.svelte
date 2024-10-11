<script lang="ts">
  import type { PageData } from '../issues/$types';
  import { pageStore } from '$lib/arch/global/PageStore';
  import FormValidator from '$lib/arch/form/FormValidator';
  import Pagination from '$lib/arch/search/Pagination.svelte';
  import SelectBox from '$lib/arch/form/SelectBox.svelte';
  import type { IssueSearchResultModel } from '$lib/arch/api/Api';
  import ApiHandler from '$lib/arch/api/ApiHandler';
  import SortOrderUtils from '$lib/arch/search/SortOrderUtils';
  import { t } from '$lib/translations';
  import DateUtils from '$lib/arch/util/DateUtils';
  import SortDirection from '$lib/arch/components/SortDirection.svelte';

  pageStore.setTitle($t('msg.issue'));

  export let data: PageData;
  let { result, condition } = data;
  const issueStatuses = data.issueStatuses;

  const resultHeaders = [
    { label: '#', key: 'id' },
    { label: $t('msg.status'), key: 'issueStatus' },
    { label: $t('msg.subject'), key: 'subject' },
    { label: $t('msg.dueDate'), key: 'dueDate' },
    { label: $t('msg.updatedAt'), key: 'updatedAt' }
  ];

  const form = FormValidator.createForm({}, search);

  async function search() {
    const r = await ApiHandler.handle<IssueSearchResultModel>(fetch, (api) =>
      api.issues.issuesSearch(condition)
    );

    if (r) {
      result = r;
    }
  }

  async function handleSort(field: string) {
    condition.sortOrders = SortOrderUtils.addSort(condition.sortOrders, field);
    condition = condition;
    await search();
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
          <label>
            <input type="checkbox" bind:checked={condition.subjectOnly} />
            {$t('msg.searchBySubjectOnly')}
          </label>
        </div>

        <div>
          <SelectBox
            id="status"
            label={$t('msg.multipleStatuses')}
            options={issueStatuses}
            multiple={true}
            bind:value={condition.issueStatuses}
          />
        </div>

        <div>
          <!-- TODO validation of range of date -->
          <label for="dueDate">{$t('msg.dueDate')}</label>
          <input type="date" bind:value={condition.dueDate} />
        </div>
      </div>
    </details>
  </form>
</section>

<section>
  <a id="newIssue" href="/issues/new"> {$t('msg.newIssue')} </a>
</section>

{#if result.list}
  <section>
    <table class="list">
      <thead>
        <tr>
          {#each resultHeaders as rh}
            <th>
              <SortDirection
                sortOrders={condition.sortOrders}
                label={rh.label}
                sortKey={rh.key}
                {handleSort}
              />
            </th>
          {/each}
        </tr>
      </thead>
      <tbody>
        {#each result.list as issue}
          <tr>
            <td><a href={`/issues/${issue.id}`}>{issue.id}</a></td>
            <td>{issue.issueStatus.name}</td>
            <td class="subject">{issue.subject}</td>
            <td>{DateUtils.date(issue.dueDate)}</td>
            <td>{DateUtils.datetime(issue.updatedAt)}</td>
          </tr>
        {/each}
      </tbody>
    </table>

    <div style="display: flex; justify-content: end;">
      <span>( {result.start}-{result.end} / {result.count} )</span>
    </div>
  </section>

  <section>
    <Pagination
      bind:currentPage={condition.pageNumber}
      lastPage={result.lastPage}
      pageNums={result.pageNums}
      handlePage={search}
    />
  </section>
{:else}
  {$t('msg.noData')}
{/if}

<style>
  table.list td:not(.subject) {
    text-align: center;
    white-space: nowrap;
  }
</style>
