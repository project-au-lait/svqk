import { test } from '@playwright/test';
import { <%= entityNmPascal %>Facade } from '@facades/<%= entityNmPascal %>Facade';
import { DryRun } from '@arch/DryRun';
import { locale } from '@arch/MultiLng';
import NumberUtils from '@arch/NumberUtils';
import TopPage from '@pages/top/TopPage';
import <%= entityNmPascal %>InputFactory from '@factories/<%= entityNmPascal %>Factory';

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
  const <%= entityNmCamel %> = <%= entityNmPascal %>InputFactory.createRandom<%= entityNmPascal %>();
  await <%= entityNmCamel %>InputPage.create(<%= entityNmCamel %>);
  await <%= entityNmCamel %>InputPage.expectSavedSuccessfully();

  // Rererence
  const <%= entityNmCamel %>Facade = new <%= entityNmPascal %>Facade(dryRun);
  <%= entityNmCamel %>InputPage = await <%= entityNmCamel %>Facade.reference<%= entityNmPascal %>ById(menuBar, <%= entityNmCamel %>.<%= idField.fieldName %>);

  // Update
  const updating<%= entityNmPascal %> = <%= entityNmPascal %>InputFactory.createRandom<%= entityNmPascal %>WithId(<%= entityNmCamel %>.<%= idField.fieldName %>);
  await <%= entityNmCamel %>InputPage.update(updating<%= entityNmPascal %>);
  await <%= entityNmCamel %>InputPage.expectSavedSuccessfully();
  await <%= entityNmCamel %>InputPage.expect<%= entityNmPascal %>(updating<%= entityNmPascal %>);

  // Delete
  await <%= entityNmCamel %>InputPage.delete();
  await <%= entityNmCamel %>InputPage.expectDeletedSuccessfully();
});