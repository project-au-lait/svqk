import { goto } from '$app/navigation';
import { test } from '@playwright/test';
import { <%= entityNmPascal %>Facade } from '../facades/<%= entityNmPascal %>Facade';
import { DryRun } from '../arch/DryRun';
import { locale } from '../arch/MultiLng';
import TopPage from '../pages/top/TopPage';
import <%= entityNmPascal %>InputFactory from '../factories/<%= entityNmPascal %>Factory';

test('CRUD of <%= entityNmPascal %>', async ({ browser }) => {
  const context = await browser.newContext({
    locale: locale
  });
  const page = await context.newPage();
  const dryRun = DryRun.build();

  const topPage = new TopPage(page, dryRun);
  const menuBar = await topPage.open();

  let <%= entityNmCamel %>ListPage = await goto(`/<%= entityNmPlural %>`);
  let <%= entityNmCamel %>InputPage = await <%= entityNmCamel %>ListPage.gotoNew<%= entityNmPascal %>Page();

  // Create
  const <%= entityNmCamel %> = <%= entityNmPascal %>InputFactory.createRandom<%= entityNmPascal %>();
  await <%= entityNmCamel %>InputPage.save(<%= entityNmCamel %>);

  // Rererence
  const <%= entityNmCamel %>Facade = new <%= entityNmPascal %>Facade(dryRun);
  const entityIdStr = <%= entityNmCamel %>.id.toString();
  <%= entityNmCamel %>InputPage = await <%= entityNmCamel %>Facade.reference<%= entityNmPascal %>ById(entityIdStr);

  // Update
  const updating<%= entityNmPascal %> = <%= entityNmPascal %>InputFactory.createRandom<%= entityNmPascal %>();
  <%= entityNmCamel %>InputPage.save(updating<%= entityNmPascal %>);
  <%= entityNmCamel %>InputPage.expect<%= entityNmPascal %>(updating<%= entityNmPascal %>);

  // TODO: Add <%= entityNmCamel %> delete step
});
