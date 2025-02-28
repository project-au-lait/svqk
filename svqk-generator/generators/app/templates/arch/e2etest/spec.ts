import { test } from '@playwright/test';
import { <%= entityNmPascal %>Facade } from '../../facades/<%= entityNmPascal %>Facade';
import { DryRun } from '@arch/DryRun';
import { locale } from '@arch/MultiLng';
import NumberUtils from '@arch/NumberUtils';
import TopPage from '../../pages/top/TopPage';
import <%= entityNmPascal %>InputFactory from '../../factories/<%= entityNmPascal %>Factory';

test('CRUD of <%= entityNmPascal %>', async ({ browser }) => {
  const context = await browser.newContext({
    locale: locale
  });

  const page = await context.newPage();
  const dryRun = DryRun.build();

  const topPage = new TopPage(page, dryRun);
  const menuBar = await topPage.open();

  let <%= entityNmCamel %>ListPage = await menuBar.goto<%= entityNmPascal %>ListPage();
  let <%= entityNmCamel %>InputPage = await <%= entityNmCamel %>ListPage.gotoNew<%= entityNmPascal %>Page();

  // Create
  const id = NumberUtils.generateRandomNumber();
  const <%= entityNmCamel %> = <%= entityNmPascal %>InputFactory.createRandom<%= entityNmPascal %>(id);
  await <%= entityNmCamel %>InputPage.save(<%= entityNmCamel %>);

  // Rererence
  const <%= entityNmCamel %>Facade = new <%= entityNmPascal %>Facade(dryRun);
  const <%= entityNmCamel %>IdStr = <%= entityNmCamel %>.id.toString();
  <%= entityNmCamel %>InputPage = await <%= entityNmCamel %>Facade.reference<%= entityNmPascal %>ById(menuBar, <%= entityNmCamel %>IdStr);

  // Update
  const updating<%= entityNmPascal %> = <%= entityNmPascal %>InputFactory.createRandom<%= entityNmPascal %>(id);
  await <%= entityNmCamel %>InputPage.save(updating<%= entityNmPascal %>);
  await <%= entityNmCamel %>InputPage.expect<%= entityNmPascal %>(updating<%= entityNmPascal %>);

  // TODO: Add <%= entityNmCamel %> delete step
});