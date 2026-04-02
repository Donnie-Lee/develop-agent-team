# 性能测试脚本模板

## 测试类型

| 测试类型 | 说明 | 工具 |
|----------|------|------|
| 负载测试 | 验证正常负载下性能 | JMeter / k6 / Gatling |
| 压力测试 | 逐步增加负载直至崩溃 | JMeter / Locust |
| 峰值测试 | 验证峰值负载下的表现 | JMeter / k6 |
| 稳定性测试 | 长时间运行稳定性 | JMeter |

---

## JMeter测试脚本模板

### 测试计划结构

```
TestPlan/
├── Thread Group - 用户组
│   ├── HTTP Request Defaults
│   ├── Login Sampler
│   ├── Home Page Sampler
│   ├── Product List Sampler
│   ├── Product Detail Sampler
│   └── Logout Sampler
├── Listeners
│   ├── Summary Report
│   ├── Aggregate Report
│   ├── View Results Tree
│   └── Graph Results
```

### JMX脚本示例

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jmeterTestPlan version="1.2" properties="5.0" jmeter="5.5">
  <hashTree>
    <TestPlan guiclass="TestPlanGui" testclass="TestPlan" testname="性能测试计划">
      <stringProp name="TestPlan.comments">系统性能测试</stringProp>
      <boolProp name="TestPlan.functionalMode">false</boolProp>
      <boolProp name="TestPlan.serialize_threadgroups">true</boolProp>
      <elementProp name="TestPlan.user_defined_variables" elementType="Arguments">
        <collectionProp name="Arguments.arguments"/>
      </elementProp>
    </TestPlan>

    <hashTree>
      <!-- 线程组配置 -->
      <ThreadGroup guiclass="ThreadGroupGui" testclass="ThreadGroup" testname="并发用户组">
        <stringProp name="ThreadGroup.num_threads">100</stringProp>
        <stringProp name="ThreadGroup.ramp_time">60</stringProp>
        <stringProp name="ThreadGroup.duration">300</stringProp>
        <stringProp name="ThreadGroup.delay"></stringProp>
      </ThreadGroup>

      <hashTree>
        <!-- HTTP默认配置 -->
        <ConfigTestElement guiclass="HttpDefaultsGui" testclass="ConfigTestElement" testname="HTTP请求默认值">
          <stringProp name="HTTPSampler.domain">localhost</stringProp>
          <stringProp name="HTTPSampler.port">8080</stringProp>
          <stringProp name="HTTPSampler.path"></stringProp>
          <boolProp name="HTTPSampler.image_parser">true</boolProp>
        </ConfigTestElement>

        <hashTree/>
      </hashTree>
    </hashTree>
  </hashTree>
</jmeterTestPlan>
```

---

## k6测试脚本模板

### 安装

```bash
npm install -g k6
```

### 负载测试脚本

```javascript
// load-test.js
import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

const errorRate = new Rate('errors');

export const options = {
  stages: [
    { duration: '2m', target: 100 },   // 逐步增加到100用户
    { duration: '5m', target: 100 },  // 保持100用户5分钟
    { duration: '2m', target: 200 },  // 增加到200用户
    { duration: '5m', target: 200 },  // 保持200用户5分钟
    { duration: '2m', target: 0 },    // 逐渐减少
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'],  // 95%请求在500ms内
    http_req_failed: ['rate<0.01'],    // 错误率小于1%
    errors: ['rate<0.1'],              // 失败率小于10%
  },
};

export default function () {
  const baseUrl = 'http://localhost:8080';

  // 登录
  const loginRes = http.post(`${baseUrl}/api/v1/user/login`, JSON.stringify({
    username: `user${Math.floor(Math.random() * 1000)}`,
    password: '123456'
  }), {
    headers: { 'Content-Type': 'application/json' },
  });

  const loginSuccess = check(loginRes, {
    '登录成功': (r) => r.status === 200,
    '返回token': (r) => r.json('data.token') !== undefined,
  });
  errorRate.add(!loginSuccess);

  if (!loginSuccess) return;

  const token = loginRes.json('data.token');

  // 获取用户信息
  const userInfoRes = http.get(`${baseUrl}/api/v1/user/info`, {
    headers: { 'Authorization': `Bearer ${token}` },
  });

  check(userInfoRes, {
    '获取用户信息成功': (r) => r.status === 200,
  });

  sleep(1);
}
```

### 压力测试脚本

```javascript
// stress-test.js
import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

const errors = new Counter('errors');

export const options = {
  scenarios: {
    constant_load: {
      executor: 'constant-vus',
      vus: 500,
      duration: '10m',
    },
    peak_load: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '5m', target: 1000 },
        { duration: '10m', target: 1000 },
        { duration: '5m', target: 0 },
      ],
    },
  },
  thresholds: {
    http_req_duration: ['p(99)<1000'],
    http_req_failed: ['rate<0.05'],
  },
};

export default function () {
  const baseUrl = 'http://localhost:8080';
  const token = 'test-token';

  const res = http.get(`${baseUrl}/api/v1/products`, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });

  check(res, {
    '接口正常': (r) => r.status === 200,
  }) || errors.add(1);

  sleep(Math.random() * 3);
}
```

---

## Gatling测试脚本模板

### Scala脚本

```scala
// LoadSimulation.scala
package com.test

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class LoadSimulation extends Simulation {

  val httpConf = http
    .baseUrl("http://localhost:8080")
    .contentTypeHeader("application/json")

  val userFeeder = csv("user-data.csv").random.circular

  val scn = scenario("用户操作流程")
    .feed(userFeeder)
    .exec(http("登录")
      .post("/api/v1/user/login")
      .body(StringBody("""{"username":"${username}","password":"123456"}"""))
      .check(jsonPath("$.data.token").saveAs("token")))
    .pause(1)
    .exec(http("获取用户信息")
      .get("/api/v1/user/info")
      .header("Authorization", session => s"Bearer ${session("token").as[String]}"))
    .pause(1)
    .exec(http("获取商品列表")
      .get("/api/v1/products"))

  setUp(
    scn.inject(
      rampUsers(1000).during(5.minutes),
      constantUsersPerSec(100).during(10.minutes)
    )
  ).protocols(httpConf)
    .assertions(
      global.responseTime.percentile(95).lt(500),
      global.successfulRequests.percent.gt(99)
    )
}
```

---

## Locust测试脚本模板

```python
# locustfile.py
from locust import HttpUser, task, between, events
import random

class WebsiteUser(HttpUser):
    wait_time = between(1, 3)
    token = None

    def on_start(self):
        """用户启动时执行"""
        response = self.client.post("/api/v1/user/login", json={
            "username": f"user_{random.randint(1, 10000)}",
            "password": "123456"
        })
        if response.status_code == 200:
            self.token = response.json().get('data', {}).get('token')

    @task(3)
    def get_product_list(self):
        """获取商品列表"""
        self.client.get("/api/v1/products")

    @task(2)
    def get_product_detail(self):
        """获取商品详情"""
        product_id = random.randint(1, 100)
        self.client.get(f"/api/v1/products/{product_id}")

    @task(1)
    def create_order(self):
        """创建订单"""
        if self.token:
            self.client.post("/api/v1/orders", json={
                "product_id": random.randint(1, 100),
                "quantity": random.randint(1, 5)
            }, headers={"Authorization": f"Bearer {self.token}"})

@events.test_start.add_listener
def on_test_start(environment, **kwargs):
    print("性能测试开始")

@events.test_stop.add_listener
def on_test_stop(environment, **kwargs):
    print("性能测试结束")
```

---

## 性能测试配置

### 测试环境配置

| 配置项 | 开发环境 | 测试环境 | 生产环境 |
|--------|----------|----------|----------|
| CPU | 4核 | 8核 | 16核+ |
| 内存 | 8GB | 16GB | 32GB+ |
| 数据库 | MySQL单实例 | MySQL主从 | MySQL集群 |
| 缓存 | Redis单实例 | Redis集群 | Redis集群 |
| 网络 | 内网 | 内网 | 负载均衡 |

### 测试数据配置

| 数据类型 | 数量 | 说明 |
|----------|------|------|
| 用户数据 | 10000+ | 真实用户数据 |
| 商品数据 | 10000+ | 真实商品数据 |
| 订单数据 | 100000+ | 历史订单数据 |

---

## 性能测试报告模板

### 测试概述

| 字段 | 内容 |
|------|------|
| 测试项目 | {项目名称} |
| 测试类型 | {负载/压力/峰值} |
| 测试时间 | {时间} |
| 测试人员 | {人员} |
| 测试环境 | {环境} |

### 测试指标

| 指标名称 | 指标值 | 要求 | 是否达标 |
|----------|--------|------|----------|
| 响应时间(平均) | {值}ms | ≤{值}ms | ✓/✗ |
| 响应时间(p95) | {值}ms | ≤{值}ms | ✓/✗ |
| 响应时间(p99) | {值}ms | ≤{值}ms | ✓/✗ |
| TPS | {值} | ≥{值} | ✓/✗ |
| 错误率 | {值}% | ≤{值}% | ✓/✗ |
| CPU使用率 | {值}% | ≤{值}% | ✓/✗ |
| 内存使用率 | {值}% | ≤{值}% | ✓/✗ |

### 性能测试结果

| 并发数 | 平均响应时间 | p95响应时间 | p99响应时间 | TPS | 错误率 |
|--------|--------------|-------------|-------------|-----|--------|
| 100 | {值}ms | {值}ms | {值}ms | {值} | {值}% |
| 200 | {值}ms | {值}ms | {值}ms | {值} | {值}% |
| 500 | {值}ms | {值}ms | {值}ms | {值} | {值}% |
| 1000 | {值}ms | {值}ms | {值}ms | {值} | {值}% |

### 结论

**测试结论**：✓ 通过 / ✗ 不通过

**原因**：
{详细说明}

**优化建议**：
{优化建议}
