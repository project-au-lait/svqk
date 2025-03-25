import fs from 'fs';
import path, { dirname } from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const PLAYWIGHT_REPORT_FILE = path.resolve(`${__dirname}/../target/test-results.json`);
const FAILSAFE_REPORT_DIR = path.resolve(`${__dirname}/../target/failsafe-reports`);
const FAILSAFE_REPORT_FILE = `${FAILSAFE_REPORT_DIR}/failsafe-summary.xml`;

if (fs.existsSync(PLAYWIGHT_REPORT_FILE)) {
  transform();
} else {
  console.log(`Playwright report does not exist: ${PLAYWIGHT_REPORT_FILE}`);
}

function transform() {
  console.log(`Transform ${PLAYWIGHT_REPORT_FILE} to ${FAILSAFE_REPORT_FILE}`);
  const data = fs.readFileSync(PLAYWIGHT_REPORT_FILE, 'utf-8');
  const report = JSON.parse(data);

  const failsafeReport = `<?xml version="1.0" encoding="UTF-8"?>
  <failsafe-summary xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="https://maven.apache.org/surefire/maven-surefire-plugin/xsd/failsafe-summary.xsd" result="null" timeout="false">
      <completed>${report.stats.expected}</completed>
      <errors>${report.errors.length}</errors>
      <failures>${report.stats.unexpected}</failures>
      <skipped>${report.stats.skipped}</skipped>
      <failureMessage xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>
  </failsafe-summary>`;

  if (!fs.existsSync(FAILSAFE_REPORT_DIR)) {
    fs.mkdirSync(FAILSAFE_REPORT_DIR);
  }

  fs.writeFileSync(FAILSAFE_REPORT_FILE, failsafeReport);
}
