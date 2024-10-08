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

  pageStore.setTitle($t('msg.issue'));

  export let data: PageData;
  $: result = data.result;
  const issueStatuses = data.issueStatuses;

  let condition = data.condition;

  const form = FormValidator.createForm({}, search);

  async function search() {
    const r = await ApiHandler.handle<IssueSearchResultModel>(fetch, (api) =>
      api.issues.issuesSearchCreate(condition)
    );

    if (r) {
      result = r;
    }
  }

  async function handleSort(field: string) {
    condition.sortOrders = SortOrderUtils.addSort(condition.sortOrders, field);
    condition = condition;
    search();
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
          <th on:click={() => handleSort('id')}
            ><span>#{SortOrderUtils.getSortMark(condition.sortOrders, 'id')}</span>
          </th>
          <th on:click={() => handleSort('issueStatus')}>
            <span>
              {$t('msg.status')}
              {SortOrderUtils.getSortMark(condition.sortOrders, 'issueStatus')}
            </span>
          </th>
          <th on:click={() => handleSort('subject')}
            ><span
              >{$t('msg.subject')}
              {SortOrderUtils.getSortMark(condition.sortOrders, 'subject')}
            </span>
          </th>
          <th on:click={() => handleSort('dueDate')}>
            <span>
              {$t('msg.dueDate')}
              {SortOrderUtils.getSortMark(condition.sortOrders, 'dueDate')}
            </span>
          </th>
          <th on:click={() => handleSort('updatedAt')}>
            <span>
              {$t('msg.updatedAt')}
              {SortOrderUtils.getSortMark(condition.sortOrders, 'updatedAt')}
            </span>
          </th>
        </tr>
      </thead>
      <tbody>
        {#each result.list as issue}
          <tr>
            <td>
              <a href={`/issues/${issue.id}`}>
                {issue.id}
              </a>
            </td>
            <td>{issue.issueStatus.name}</td>
            <td>{issue.subject}</td>
            <td>{DateUtils.date(issue.dueDate)}</td>
            <td class="updatedAt">{DateUtils.datetime(issue.updatedAt)}</td>
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
  th span {
    cursor: pointer;
  }

  table.list td.updatedAt {
    white-space: nowrap;
  }
</style>
