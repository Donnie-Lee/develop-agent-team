# 自动化测试脚本模板

## 项目结构

```
automation/
├── config/
│   ├── env.js              # 环境配置
│   └── data.js             # 测试数据配置
├── tests/
│   ├── api/               # 接口自动化测试
│   │   ├── login.test.js
│   │   └── order.test.js
│   ├── web/               # Web端自动化测试
│   │   ├── login.spec.js
│   │   └── dashboard.spec.js
│   └── mobile/            # 移动端自动化测试
│       └── login.spec.js
├── reports/               # 测试报告
├── scripts/
│   └── run-tests.js       # 执行脚本
├── package.json
└── README.md
```

---

## 接口自动化测试模板

### 使用工具

- **框架**：REST Assured (Java) / PyTest (Python) / Jest (Node.js)
- **工具**：Postman + Newman

### Java + REST Assured 示例

```java
package com.test.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/api/v1";
    }

    @Test(description = "用户登录接口测试")
    public void testUserLogin() {
        given()
            .contentType("application/json")
            .body("{\"username\":\"testuser\",\"password\":\"123456\"}")
        .when()
            .post("/user/login")
        .then()
            .statusCode(200)
            .body("code", equalTo(0))
            .body("data.token", notNullValue());
    }

    @Test(description = "获取用户信息接口测试")
    public void testGetUserInfo() {
        String token = getToken();

        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/user/info")
        .then()
            .statusCode(200)
            .body("code", equalTo(0))
            .body("data.username", notNullValue());
    }
}
```

### Postman + Newman 示例

```json
{
  "info": {
    "name": "API自动化测试集合",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "用户登录",
      "event": [
        {
          "listen": "test",
          "script": {
            "exec": [
              "pm.test('状态码为200', function() {",
              "    pm.response.to.have.status(200);",
              "});",
              "pm.test('返回token', function() {",
              "    var jsonData = pm.response.json();",
              "    pm.expect(jsonData.data.token).to.not.be.null;",
              "});"
            ]
          }
        }
      ],
      "request": {
        "method": "POST",
        "header": [],
        "body": {
          "mode": "raw",
          "raw": "{\"username\":\"testuser\",\"password\":\"123456\"}"
        },
        "url": {
          "raw": "{{baseUrl}}/api/v1/user/login",
          "host": ["{{baseUrl}}"],
          "path": ["api", "v1", "user", "login"]
        }
      }
    }
  ]
}
```

---

## Web端自动化测试模板

### 使用工具

- **框架**：Cypress / Playwright / Selenium

### Cypress 示例

```javascript
// cypress/e2e/login.cy.js

describe('登录功能自动化测试', () => {
  beforeEach(() => {
    cy.visit('/login')
  })

  it('正常登录', () => {
    cy.get('[data-testid=username-input]').type('testuser')
    cy.get('[data-testid=password-input]').type('123456')
    cy.get('[data-testid=login-button]').click()
    cy.url().should('include', '/dashboard')
    cy.get('[data-testid=welcome-message]').should('contain', '欢迎')
  })

  it('密码错误登录失败', () => {
    cy.get('[data-testid=username-input]').type('testuser')
    cy.get('[data-testid=password-input]').type('wrongpassword')
    cy.get('[data-testid=login-button]').click()
    cy.get('[data-testid=error-message]').should('be.visible')
    cy.get('[data-testid=error-message]').should('contain', '用户名或密码错误')
  })

  it('用户名为空登录失败', () => {
    cy.get('[data-testid=password-input]').type('123456')
    cy.get('[data-testid=login-button]').click()
    cy.get('[data-testid=username-error]').should('contain', '请输入用户名')
  })
})
```

### Playwright 示例

```javascript
// playwright/tests/login.spec.js
const { test, expect } = require('@playwright/test');

test.describe('登录功能测试', () => {
  test('正常登录', async ({ page }) => {
    await page.goto('/login');
    await page.fill('[data-testid=username-input]', 'testuser');
    await page.fill('[data-testid=password-input]', '123456');
    await page.click('[data-testid=login-button]');
    await expect(page).toHaveURL(/.*\/dashboard/);
  });

  test('密码错误', async ({ page }) => {
    await page.goto('/login');
    await page.fill('[data-testid=username-input]', 'testuser');
    await page.fill('[data-testid=password-input]', 'wrongpassword');
    await page.click('[data-testid=login-button]');
    await expect(page.locator('[data-testid=error-message]')).toBeVisible();
  });
});
```

---

## 移动端自动化测试模板

### React Native (Detox)

```javascript
// e2e/src/login.spec.js

describe('登录功能测试', () => {
  beforeEach(async () => {
    await device.reloadReactNative();
  });

  it('正常登录', async () => {
    await element(by.id('usernameInput')).typeText('testuser');
    await element(by.id('passwordInput')).typeText('123456');
    await element(by.id('loginButton')).tap();
    await expect(element(by.id('dashboardScreen'))).toBeVisible();
  });

  it('密码错误', async () => {
    await element(by.id('usernameInput')).typeText('testuser');
    await element(by.id('passwordInput')).typeText('wrongpassword');
    await element(by.id('loginButton')).tap();
    await expect(element(by.text('用户名或密码错误'))).toBeVisible();
  });
});
```

### Flutter (Integration Test)

```dart
// integration_test/app_test.dart

import 'package:flutter_test/flutter_test.dart';
import 'package:integration_test/integration_test.dart';
import 'package:flutter_app/main.dart' as app;

void main() {
  IntegrationTestWidgetsFlutterBinding.ensureInitialized();

  group('登录功能测试', () {
    testWidgets('正常登录', (WidgetTester tester) async {
      app.main();
      await tester.pumpAndSettle();

      await tester.enterText(find.byKey(Key('username')), 'testuser');
      await tester.enterText(find.byKey(Key('password')), '123456');
      await tester.tap(find.byKey(Key('loginButton')));
      await tester.pumpAndSettle();

      expect(find.text('Dashboard'), findsOneWidget);
    });
  });
}
```

---

## 小程序自动化测试模板

### 使用工具：Miniprogram-automator

```javascript
// miniprogram/test/login.spec.js

const automator = require('miniprogram-automator');

describe('登录功能测试', () => {
  let miniProgram;

  beforeAll(async () => {
    miniProgram = await automator.launch({
      projectPath: './miniprogram'
    });
  }, 30000);

  afterAll(async () => {
    await miniProgram.close();
  });

  it('正常登录', async () => {
    const page = await miniProgram.reLaunch('/pages/login/login');
    await page.waitFor('#username');

    await page.setData({
      username: 'testuser',
      password: '123456'
    });

    await page.tap('#loginBtn');
    await page.waitForNavigation();

    expect(page.path).toBe('/pages/index/index');
  });
});
```

---

## 执行脚本

### 运行所有测试

```bash
#!/bin/bash

# 运行接口自动化测试
echo "Running API tests..."
newman run api-tests.postman_collection.json -e api-env.json --reporters html,junit --reporter-junit-export reports/api-results.xml

# 运行Web端自动化测试
echo "Running Web UI tests..."
npx cypress run --spec "cypress/e2e/**/*.cy.js" --reporter mochawesome

# 运行测试报告生成
echo "Generating test reports..."
node scripts/generate-report.js
```

### CI/CD集成

```yaml
# .gitlab-ci.yml
stages:
  - test

自动化测试:
  stage: test
  script:
    - npm install
    - npm run api-test
    - npm run web-test
  artifacts:
    reports:
      junit: reports/*.xml
    paths:
      - reports/
```

---

## 测试报告生成

### Allure报告

```bash
# 生成Allure报告
allure generate reports/ -o allure-report/
allure serve allure-report/
```

### 测试报告汇总

| 测试类型 | 用例数 | 通过数 | 失败数 | 通过率 | 执行时间 |
|----------|--------|--------|--------|--------|----------|
| 接口测试 | {数} | {数} | {数} | {率} | {时间} |
| Web测试 | {数} | {数} | {数} | {率} | {时间} |
| 移动端测试 | {数} | {数} | {数} | {率} | {时间} |
| **合计** | **{数}** | **{数}** | **{数}** | **{率}** | **{时间}** |
