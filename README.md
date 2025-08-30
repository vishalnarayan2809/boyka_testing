<div align="center">

# SauceDemo Automated UI Tests (Boyka + TestNG + Maven)

Simple, reliable, reproducible end‑to‑end (E2E) UI tests for https://www.saucedemo.com using the Boyka Framework.

</div>

---

## 1. Quick Start (TL;DR)

```bash
# 1. Install Java 17 + Maven + Chrome (latest)
# 2. Clone
git clone <your-fork-or-clone-url> boyka-saucedemo
cd boyka-saucedemo

# 3. Build & run all tests
mvn clean test

# 4. Open reports
start target/surefire-reports/index.html   # Windows
```

If that worked, you are done. For deeper detail, keep reading.

---

## 2. Prerequisites (Exact Versions / Checks)

| Tool | Minimum | How to verify | Install Hint |
|------|---------|---------------|--------------|
| Java | 17 (LTS) | `java -version` | AdoptOpenJDK / Temurin |
| Maven | 3.8+ | `mvn -v` | https://maven.apache.org/download.cgi |
| Chrome | Latest stable | Open Chrome > Settings > About | https://google.com/chrome |
| Git | Any recent | `git --version` | https://git-scm.com |

Optional (nice to have): An IDE like IntelliJ IDEA or VS Code with Java extensions.

> Tip: Ensure JAVA_HOME points to the JDK 17 root.

---

## 3. Project Layout

```
boyka-config.json         # Main Boyka runtime config (browser, timeouts, options)
pom.xml                   # Maven dependencies + plugins
testng.xml                # TestNG suite definition
scripts/disable_chrome_password_leak.ps1  # (Optional) Policy script to silence Chrome password alerts
src/test/java/com/saucedemo/tests/SauceDemoTests.java  # All test cases
logs/                     # Runtime logs (configured in boyka-config)
target/surefire-reports/  # TestNG + Surefire reports after a run
```

---

## 4. Installing & Building

1. Clone
```bash
git clone <your-fork-or-clone-url>
cd <repo-folder>
```
2. First build (downloads Boyka + TestNG):
```bash
mvn clean install -DskipTests
```
3. Run tests:
```bash
mvn test
```

> The first run may take a bit longer (dependency & driver downloads). Subsequent runs are faster.

---

## 5. Configuration (`boyka-config.json` Simplified Guide)

Key block used by tests: `web.test_web`.

| Field | Purpose | Current Value |
|-------|---------|---------------|
| browser | Target browser | chrome |
| runType | Execution mode | local |
| headless | Run headless? | false |
| baseUrl | Aut under test | https://www.saucedemo.com |
| timeout.implicitWait | Selenium implicit wait (s) | 10 |
| timeout.explicitWait | Boyka explicit wait (s) | 20 |
| screenshot.onFailure | Capture failures | true |

Chrome hardening flags suppress password / notification popups. Adjust only if you see navigation issues.

### Change to Headless (CI / server):
Set `"headless": true` under `test_web` OR run with:
```bash
mvn test -Dheadless=true   # if your build tooling maps this property
```

### Switch Browser (example Firefox)
Edit `browser` to `firefox` (ensure Firefox installed). Additional config may be required for Edge/Safari.

---

## 6. Test Suite

Single class: `SauceDemoTests` containing 5 scenarios:
1. Valid login
2. Invalid login
3. Add product to cart
4. Remove product from cart
5. Complete checkout flow

Run patterns:
```bash
# All tests (suite)
mvn test

# Specific class
mvn test -Dtest=SauceDemoTests

# Single test method (example)
mvn test -Dtest=SauceDemoTests#testValidLogin
```

---

## 7. Outputs & Artifacts

| Location | What you get |
|----------|--------------|
| target/surefire-reports/ | XML + HTML reports (index.html, emailable-report.html) |
| logs/ | Framework logs per run (rotated) |
| screenshots/ | Failure screenshots (if any) |

Open `target/surefire-reports/index.html` after a run for a visual summary.

---

## 8. Disabling Chrome Password / Leak Popups (Optional)

Already minimized via runtime flags & preferences. If you still see a breach/password manager prompt:

Run the PowerShell script as Administrator (Windows only):
```powershell
Set-ExecutionPolicy Bypass -Scope Process -Force
./scripts/disable_chrome_password_leak.ps1
```
Restart Chrome afterwards. Policies written under `HKLM:\SOFTWARE\Policies\Google\Chrome`.

---

## 9. Troubleshooting Quick Table

| Symptom | Likely Cause | Fix |
|---------|--------------|-----|
| net::ERR_CONNECTION_RESET during first navigation | Transient network / aggressive flags | Re-run (setup auto-retries once). Ensure stable internet. Simplify flags if persistent. |
| Elements intermittently not found right after login | Page still rendering | Waits already added; increase explicit wait to 30 in config if needed. |
| No logs written | Logging level or path issue | Check `logging.level` and `logging.path` in config. |
| NOP SLF4J logger warning | No binding on classpath | Add a logging backend (e.g., logback-classic) if richer logs needed. |
| Need headless in CI | headless false | Set `headless` true in config or supply property. |

---

## 10. Extending Tests

Add a new scenario:
1. Create a new method in `SauceDemoTests` with `@Test(priority = X)`.
2. Reuse existing locators or create new `Locator.buildLocator()...` definitions.
3. Keep actions atomic: login helpers, add/remove helpers exist.
4. Run `mvn test -Dtest=SauceDemoTests#yourNewMethod`.

For larger growth, consider splitting into multiple classes (e.g., `LoginTests`, `CartTests`) and list them in `testng.xml`.

---

## 11. Updating Dependencies Safely

1. Check latest Boyka + Selenium compatibility (CDP warnings may prompt update).
2. Edit versions in `pom.xml` under `<properties>`.
3. Run: `mvn clean verify`.
4. If failures arise, inspect `target/surefire-reports/*.txt`.

---

## 12. Clean Environment Reset

If drivers/cache cause issues:
```bash
git clean -fdx   # WARNING: removes untracked files (ensure you have no local only work)
git restore .
```
Or just delete `target/` and re-run `mvn test`.

---

## 13. FAQ

Q: Do I need to download ChromeDriver manually?
A: No. Boyka / Selenium Manager handle it automatically.

Q: Can I run in parallel?
A: Add more test classes; Boyka manages sessions per method. Then configure TestNG parallel in `testng.xml` (`parallel="methods" thread-count="X"`).

Q: Where do screenshots go?
A: `./screenshots` (configurable in `boyka-config.json`).

---

## 14. License

MIT (see LICENSE if present). Use freely for learning or as a template.

---

## 15. Support / Contributions

Issues & PRs welcome. Keep changes small and include rationale in the PR description.

---

Happy Testing & Shipping! 🚀
