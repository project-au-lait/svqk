import { expect, test } from '@playwright/test';

test('Go to /hello then World is displayed', async ({ page }) => {
  await page.goto('/hello');
  await expect(page.locator('#message')).toHaveText('World');
});
