import { expect, test } from "@playwright/test";

test("Go to /<%= entityNmCamel %> then data is displayed", async ({ page }) => {
  await page.goto("/<%= entityNmCamel %>");
  await expect(page.locator("#status")).toHaveText("200");
});
