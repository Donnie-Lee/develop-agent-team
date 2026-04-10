# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: admin.spec.js >> Admin题库管理模块 >> ADM-QUES-005: 编辑题目
- Location: tests/admin.spec.js:205:3

# Error details

```
Error: expect(locator).toBeVisible() failed

Locator: locator('.el-dialog')
Expected: visible
Timeout: 5000ms
Error: element(s) not found

Call log:
  - Expect "toBeVisible" with timeout 5000ms
  - waiting for locator('.el-dialog')

```

# Page snapshot

```yaml
- generic [ref=e4]:
  - complementary [ref=e5]:
    - generic [ref=e6]:
      - img "InterviewAI" [ref=e7]
      - generic [ref=e8]: 管理后台
    - menubar [ref=e9]:
      - menuitem "控制台" [ref=e10] [cursor=pointer]:
        - img [ref=e12]
        - generic [ref=e14]: 控制台
      - menuitem "用户管理" [ref=e15] [cursor=pointer]:
        - img [ref=e17]
        - generic [ref=e19]: 用户管理
      - menuitem "简历管理" [ref=e20] [cursor=pointer]:
        - img [ref=e22]
        - generic [ref=e24]: 简历管理
      - menuitem "面试管理" [ref=e25] [cursor=pointer]:
        - img [ref=e27]
        - generic [ref=e29]: 面试管理
      - menuitem "题库管理" [ref=e30] [cursor=pointer]:
        - img [ref=e32]
        - generic [ref=e35]: 题库管理
      - menuitem "系统设置" [ref=e36] [cursor=pointer]:
        - img [ref=e38]
        - generic [ref=e40]: 系统设置
  - generic [ref=e41]:
    - generic [ref=e42]:
      - heading "题库管理" [level=2] [ref=e44]
      - button "管理员" [ref=e47] [cursor=pointer]:
        - img [ref=e50]
        - generic [ref=e52]: 管理员
    - main [ref=e53]:
      - generic [ref=e55]:
        - generic [ref=e56]:
          - heading "题库管理" [level=3] [ref=e57]
          - button "添加题目" [ref=e59] [cursor=pointer]:
            - generic [ref=e60]: 添加题目
        - generic [ref=e62]:
          - table [ref=e64]:
            - rowgroup [ref=e73]:
              - row "ID 题目标题 分类 难度 类型 浏览次数 操作" [ref=e74]:
                - columnheader "ID" [ref=e75]:
                  - generic [ref=e76]: ID
                - columnheader "题目标题" [ref=e77]:
                  - generic [ref=e78]: 题目标题
                - columnheader "分类" [ref=e79]:
                  - generic [ref=e80]: 分类
                - columnheader "难度" [ref=e81]:
                  - generic [ref=e82]: 难度
                - columnheader "类型" [ref=e83]:
                  - generic [ref=e84]: 类型
                - columnheader "浏览次数" [ref=e85]:
                  - generic [ref=e86]: 浏览次数
                - columnheader "操作" [ref=e87]:
                  - generic [ref=e88]: 操作
          - table [ref=e93]:
            - rowgroup [ref=e102]:
              - row "2 闭包概念 frontend simple discussion 0 编辑 删除" [ref=e103]:
                - cell "2" [ref=e104]:
                  - generic [ref=e105]: "2"
                - cell "闭包概念" [ref=e106]:
                  - generic [ref=e107]: 闭包概念
                - cell "frontend" [ref=e108]:
                  - generic [ref=e109]: frontend
                - cell "simple" [ref=e110]:
                  - generic [ref=e113]: simple
                - cell "discussion" [ref=e114]:
                  - generic [ref=e115]: discussion
                - cell "0" [ref=e116]:
                  - generic [ref=e117]: "0"
                - cell "编辑 删除" [ref=e118]:
                  - generic [ref=e119]:
                    - button "编辑" [ref=e120] [cursor=pointer]:
                      - generic [ref=e121]: 编辑
                    - button "删除" [ref=e122] [cursor=pointer]:
                      - generic [ref=e123]: 删除
              - row "3 CSS Flexbox frontend simple discussion 0 编辑 删除" [ref=e124]:
                - cell "3" [ref=e125]:
                  - generic [ref=e126]: "3"
                - cell "CSS Flexbox" [ref=e127]:
                  - generic [ref=e128]: CSS Flexbox
                - cell "frontend" [ref=e129]:
                  - generic [ref=e130]: frontend
                - cell "simple" [ref=e131]:
                  - generic [ref=e134]: simple
                - cell "discussion" [ref=e135]:
                  - generic [ref=e136]: discussion
                - cell "0" [ref=e137]:
                  - generic [ref=e138]: "0"
                - cell "编辑 删除" [ref=e139]:
                  - generic [ref=e140]:
                    - button "编辑" [ref=e141] [cursor=pointer]:
                      - generic [ref=e142]: 编辑
                    - button "删除" [ref=e143] [cursor=pointer]:
                      - generic [ref=e144]: 删除
              - row "4 HTTP缓存 frontend medium discussion 0 编辑 删除" [ref=e145]:
                - cell "4" [ref=e146]:
                  - generic [ref=e147]: "4"
                - cell "HTTP缓存" [ref=e148]:
                  - generic [ref=e149]: HTTP缓存
                - cell "frontend" [ref=e150]:
                  - generic [ref=e151]: frontend
                - cell "medium" [ref=e152]:
                  - generic [ref=e155]: medium
                - cell "discussion" [ref=e156]:
                  - generic [ref=e157]: discussion
                - cell "0" [ref=e158]:
                  - generic [ref=e159]: "0"
                - cell "编辑 删除" [ref=e160]:
                  - generic [ref=e161]:
                    - button "编辑" [ref=e162] [cursor=pointer]:
                      - generic [ref=e163]: 编辑
                    - button "删除" [ref=e164] [cursor=pointer]:
                      - generic [ref=e165]: 删除
              - row "5 Git rebase vs merge frontend medium discussion 0 编辑 删除" [ref=e166]:
                - cell "5" [ref=e167]:
                  - generic [ref=e168]: "5"
                - cell "Git rebase vs merge" [ref=e169]:
                  - generic [ref=e170]: Git rebase vs merge
                - cell "frontend" [ref=e171]:
                  - generic [ref=e172]: frontend
                - cell "medium" [ref=e173]:
                  - generic [ref=e176]: medium
                - cell "discussion" [ref=e177]:
                  - generic [ref=e178]: discussion
                - cell "0" [ref=e179]:
                  - generic [ref=e180]: "0"
                - cell "编辑 删除" [ref=e181]:
                  - generic [ref=e182]:
                    - button "编辑" [ref=e183] [cursor=pointer]:
                      - generic [ref=e184]: 编辑
                    - button "删除" [ref=e185] [cursor=pointer]:
                      - generic [ref=e186]: 删除
              - row "6 JavaScript类型转换 frontend hard multiple_choice 0 编辑 删除" [ref=e187]:
                - cell "6" [ref=e188]:
                  - generic [ref=e189]: "6"
                - cell "JavaScript类型转换" [ref=e190]:
                  - generic [ref=e191]: JavaScript类型转换
                - cell "frontend" [ref=e192]:
                  - generic [ref=e193]: frontend
                - cell "hard" [ref=e194]:
                  - generic [ref=e197]: hard
                - cell "multiple_choice" [ref=e198]:
                  - generic [ref=e199]: multiple_choice
                - cell "0" [ref=e200]:
                  - generic [ref=e201]: "0"
                - cell "编辑 删除" [ref=e202]:
                  - generic [ref=e203]:
                    - button "编辑" [ref=e204] [cursor=pointer]:
                      - generic [ref=e205]: 编辑
                    - button "删除" [ref=e206] [cursor=pointer]:
                      - generic [ref=e207]: 删除
              - row "7 React Hooks frontend medium discussion 0 编辑 删除" [ref=e208]:
                - cell "7" [ref=e209]:
                  - generic [ref=e210]: "7"
                - cell "React Hooks" [ref=e211]:
                  - generic [ref=e212]: React Hooks
                - cell "frontend" [ref=e213]:
                  - generic [ref=e214]: frontend
                - cell "medium" [ref=e215]:
                  - generic [ref=e218]: medium
                - cell "discussion" [ref=e219]:
                  - generic [ref=e220]: discussion
                - cell "0" [ref=e221]:
                  - generic [ref=e222]: "0"
                - cell "编辑 删除" [ref=e223]:
                  - generic [ref=e224]:
                    - button "编辑" [ref=e225] [cursor=pointer]:
                      - generic [ref=e226]: 编辑
                    - button "删除" [ref=e227] [cursor=pointer]:
                      - generic [ref=e228]: 删除
              - row "8 SQL注入防范 backend medium discussion 0 编辑 删除" [ref=e229]:
                - cell "8" [ref=e230]:
                  - generic [ref=e231]: "8"
                - cell "SQL注入防范" [ref=e232]:
                  - generic [ref=e233]: SQL注入防范
                - cell "backend" [ref=e234]:
                  - generic [ref=e235]: backend
                - cell "medium" [ref=e236]:
                  - generic [ref=e239]: medium
                - cell "discussion" [ref=e240]:
                  - generic [ref=e241]: discussion
                - cell "0" [ref=e242]:
                  - generic [ref=e243]: "0"
                - cell "编辑 删除" [ref=e244]:
                  - generic [ref=e245]:
                    - button "编辑" [ref=e246] [cursor=pointer]:
                      - generic [ref=e247]: 编辑
                    - button "删除" [ref=e248] [cursor=pointer]:
                      - generic [ref=e249]: 删除
              - row "9 Redis数据类型 backend simple multiple_choice 0 编辑 删除" [ref=e250]:
                - cell "9" [ref=e251]:
                  - generic [ref=e252]: "9"
                - cell "Redis数据类型" [ref=e253]:
                  - generic [ref=e254]: Redis数据类型
                - cell "backend" [ref=e255]:
                  - generic [ref=e256]: backend
                - cell "simple" [ref=e257]:
                  - generic [ref=e260]: simple
                - cell "multiple_choice" [ref=e261]:
                  - generic [ref=e262]: multiple_choice
                - cell "0" [ref=e263]:
                  - generic [ref=e264]: "0"
                - cell "编辑 删除" [ref=e265]:
                  - generic [ref=e266]:
                    - button "编辑" [ref=e267] [cursor=pointer]:
                      - generic [ref=e268]: 编辑
                    - button "删除" [ref=e269] [cursor=pointer]:
                      - generic [ref=e270]: 删除
              - row "10 进程与线程 backend simple multiple_choice 0 编辑 删除" [ref=e271]:
                - cell "10" [ref=e272]:
                  - generic [ref=e273]: "10"
                - cell "进程与线程" [ref=e274]:
                  - generic [ref=e275]: 进程与线程
                - cell "backend" [ref=e276]:
                  - generic [ref=e277]: backend
                - cell "simple" [ref=e278]:
                  - generic [ref=e281]: simple
                - cell "multiple_choice" [ref=e282]:
                  - generic [ref=e283]: multiple_choice
                - cell "0" [ref=e284]:
                  - generic [ref=e285]: "0"
                - cell "编辑 删除" [ref=e286]:
                  - generic [ref=e287]:
                    - button "编辑" [ref=e288] [cursor=pointer]:
                      - generic [ref=e289]: 编辑
                    - button "删除" [ref=e290] [cursor=pointer]:
                      - generic [ref=e291]: 删除
              - row "11 JWT认证 backend simple multiple_choice 0 编辑 删除" [ref=e292]:
                - cell "11" [ref=e293]:
                  - generic [ref=e294]: "11"
                - cell "JWT认证" [ref=e295]:
                  - generic [ref=e296]: JWT认证
                - cell "backend" [ref=e297]:
                  - generic [ref=e298]: backend
                - cell "simple" [ref=e299]:
                  - generic [ref=e302]: simple
                - cell "multiple_choice" [ref=e303]:
                  - generic [ref=e304]: multiple_choice
                - cell "0" [ref=e305]:
                  - generic [ref=e306]: "0"
                - cell "编辑 删除" [ref=e307]:
                  - generic [ref=e308]:
                    - button "编辑" [ref=e309] [cursor=pointer]:
                      - generic [ref=e310]: 编辑
                    - button "删除" [ref=e311] [cursor=pointer]:
                      - generic [ref=e312]: 删除
              - row "12 数据库索引 backend hard discussion 0 编辑 删除" [ref=e313]:
                - cell "12" [ref=e314]:
                  - generic [ref=e315]: "12"
                - cell "数据库索引" [ref=e316]:
                  - generic [ref=e317]: 数据库索引
                - cell "backend" [ref=e318]:
                  - generic [ref=e319]: backend
                - cell "hard" [ref=e320]:
                  - generic [ref=e323]: hard
                - cell "discussion" [ref=e324]:
                  - generic [ref=e325]: discussion
                - cell "0" [ref=e326]:
                  - generic [ref=e327]: "0"
                - cell "编辑 删除" [ref=e328]:
                  - generic [ref=e329]:
                    - button "编辑" [ref=e330] [cursor=pointer]:
                      - generic [ref=e331]: 编辑
                    - button "删除" [ref=e332] [cursor=pointer]:
                      - generic [ref=e333]: 删除
              - row "13 Docker容器重启策略 backend medium multiple_choice 0 编辑 删除" [ref=e334]:
                - cell "13" [ref=e335]:
                  - generic [ref=e336]: "13"
                - cell "Docker容器重启策略" [ref=e337]:
                  - generic [ref=e338]: Docker容器重启策略
                - cell "backend" [ref=e339]:
                  - generic [ref=e340]: backend
                - cell "medium" [ref=e341]:
                  - generic [ref=e344]: medium
                - cell "multiple_choice" [ref=e345]:
                  - generic [ref=e346]: multiple_choice
                - cell "0" [ref=e347]:
                  - generic [ref=e348]: "0"
                - cell "编辑 删除" [ref=e349]:
                  - generic [ref=e350]:
                    - button "编辑" [ref=e351] [cursor=pointer]:
                      - generic [ref=e352]: 编辑
                    - button "删除" [ref=e353] [cursor=pointer]:
                      - generic [ref=e354]: 删除
              - row "14 TypeScript泛型 frontend medium discussion 0 编辑 删除" [ref=e355]:
                - cell "14" [ref=e356]:
                  - generic [ref=e357]: "14"
                - cell "TypeScript泛型" [ref=e358]:
                  - generic [ref=e359]: TypeScript泛型
                - cell "frontend" [ref=e360]:
                  - generic [ref=e361]: frontend
                - cell "medium" [ref=e362]:
                  - generic [ref=e365]: medium
                - cell "discussion" [ref=e366]:
                  - generic [ref=e367]: discussion
                - cell "0" [ref=e368]:
                  - generic [ref=e369]: "0"
                - cell "编辑 删除" [ref=e370]:
                  - generic [ref=e371]:
                    - button "编辑" [ref=e372] [cursor=pointer]:
                      - generic [ref=e373]: 编辑
                    - button "删除" [ref=e374] [cursor=pointer]:
                      - generic [ref=e375]: 删除
              - row "15 性能优化策略 frontend medium discussion 0 编辑 删除" [ref=e376]:
                - cell "15" [ref=e377]:
                  - generic [ref=e378]: "15"
                - cell "性能优化策略" [ref=e379]:
                  - generic [ref=e380]: 性能优化策略
                - cell "frontend" [ref=e381]:
                  - generic [ref=e382]: frontend
                - cell "medium" [ref=e383]:
                  - generic [ref=e386]: medium
                - cell "discussion" [ref=e387]:
                  - generic [ref=e388]: discussion
                - cell "0" [ref=e389]:
                  - generic [ref=e390]: "0"
                - cell "编辑 删除" [ref=e391]:
                  - generic [ref=e392]:
                    - button "编辑" [ref=e393] [cursor=pointer]:
                      - generic [ref=e394]: 编辑
                    - button "删除" [ref=e395] [cursor=pointer]:
                      - generic [ref=e396]: 删除
              - row "16 微服务通信方式 backend medium multiple_choice 0 编辑 删除" [ref=e397]:
                - cell "16" [ref=e398]:
                  - generic [ref=e399]: "16"
                - cell "微服务通信方式" [ref=e400]:
                  - generic [ref=e401]: 微服务通信方式
                - cell "backend" [ref=e402]:
                  - generic [ref=e403]: backend
                - cell "medium" [ref=e404]:
                  - generic [ref=e407]: medium
                - cell "multiple_choice" [ref=e408]:
                  - generic [ref=e409]: multiple_choice
                - cell "0" [ref=e410]:
                  - generic [ref=e411]: "0"
                - cell "编辑 删除" [ref=e412]:
                  - generic [ref=e413]:
                    - button "编辑" [ref=e414] [cursor=pointer]:
                      - generic [ref=e415]: 编辑
                    - button "删除" [ref=e416] [cursor=pointer]:
                      - generic [ref=e417]: 删除
              - row "17 Go GMP模型 backend hard discussion 0 编辑 删除" [ref=e418]:
                - cell "17" [ref=e419]:
                  - generic [ref=e420]: "17"
                - cell "Go GMP模型" [ref=e421]:
                  - generic [ref=e422]: Go GMP模型
                - cell "backend" [ref=e423]:
                  - generic [ref=e424]: backend
                - cell "hard" [ref=e425]:
                  - generic [ref=e428]: hard
                - cell "discussion" [ref=e429]:
                  - generic [ref=e430]: discussion
                - cell "0" [ref=e431]:
                  - generic [ref=e432]: "0"
                - cell "编辑 删除" [ref=e433]:
                  - generic [ref=e434]:
                    - button "编辑" [ref=e435] [cursor=pointer]:
                      - generic [ref=e436]: 编辑
                    - button "删除" [ref=e437] [cursor=pointer]:
                      - generic [ref=e438]: 删除
              - row "1 Vue响应式原理 frontend medium discussion 12 编辑 删除" [ref=e439]:
                - cell "1" [ref=e440]:
                  - generic [ref=e441]: "1"
                - cell "Vue响应式原理" [ref=e442]:
                  - generic [ref=e443]: Vue响应式原理
                - cell "frontend" [ref=e444]:
                  - generic [ref=e445]: frontend
                - cell "medium" [ref=e446]:
                  - generic [ref=e449]: medium
                - cell "discussion" [ref=e450]:
                  - generic [ref=e451]: discussion
                - cell "12" [ref=e452]:
                  - generic [ref=e453]: "12"
                - cell "编辑 删除" [ref=e454]:
                  - generic [ref=e455]:
                    - button "编辑" [ref=e456] [cursor=pointer]:
                      - generic [ref=e457]: 编辑
                    - button "删除" [ref=e458] [cursor=pointer]:
                      - generic [ref=e459]: 删除
        - generic [ref=e460]:
          - generic [ref=e461]: Total 17
          - generic [ref=e464] [cursor=pointer]:
            - generic:
              - combobox [ref=e466]
              - generic [ref=e467]: 10/page
            - img [ref=e470]
          - button "Go to previous page" [disabled] [ref=e472]:
            - generic:
              - img
          - list [ref=e473]:
            - listitem "page 1" [ref=e474]: "1"
            - listitem "page 2" [ref=e475] [cursor=pointer]: "2"
          - button "Go to next page" [ref=e476] [cursor=pointer]:
            - generic:
              - img
          - generic [ref=e477]:
            - generic [ref=e478]: Go to
            - spinbutton "Page" [ref=e481]: "1"
```

# Test source

```ts
  118 |     await page.waitForTimeout(3000);
  119 | 
  120 |     // 验证活动表格存在
  121 |     const table = dashboardPage.recentActivitiesTable;
  122 |     await expect(table).toBeVisible();
  123 | 
  124 |     log.success('ADM-DASH-002 测试完成');
  125 |   });
  126 | 
  127 |   test('ADM-DASH-003: 数据刷新', async ({ page }) => {
  128 |     log.info('测试数据刷新');
  129 |     await dashboardPage.goto();
  130 |     await dashboardPage.clickRefresh();
  131 | 
  132 |     await page.waitForTimeout(1000);
  133 | 
  134 |     log.success('ADM-DASH-003 测试完成');
  135 |   });
  136 | });
  137 | 
  138 | // ========== 题库管理模块测试 ==========
  139 | 
  140 | test.describe('Admin题库管理模块', () => {
  141 |   let loginPage;
  142 |   let questionPage;
  143 | 
  144 |   test.beforeEach(async ({ page }) => {
  145 |     loginPage = new AdminLoginPage(page);
  146 |     questionPage = new AdminQuestionManagementPage(page);
  147 | 
  148 |     // 先登录
  149 |     await page.goto(BASE_URL + '/login');
  150 |     await loginPage.loginWithUniversalCode(TEST_CONFIG.testPhone);
  151 |     await page.waitForTimeout(2000);
  152 |   });
  153 | 
  154 |   test('ADM-QUES-001: 题目列表加载', async ({ page }) => {
  155 |     log.info('测试题目列表加载');
  156 |     await questionPage.goto();
  157 | 
  158 |     // 等待数据加载
  159 |     await page.waitForTimeout(3000);
  160 | 
  161 |     // 验证表格存在
  162 |     const table = questionPage.questionTable;
  163 |     await expect(table).toBeVisible();
  164 | 
  165 |     log.success('ADM-QUES-001 测试完成');
  166 |   });
  167 | 
  168 |   test('ADM-QUES-003: 添加题目-打开弹窗', async ({ page }) => {
  169 |     log.info('测试添加题目-打开弹窗');
  170 |     await questionPage.goto();
  171 |     await page.waitForTimeout(2000);
  172 | 
  173 |     await questionPage.clickAdd();
  174 | 
  175 |     // 验证弹窗打开
  176 |     const dialog = questionPage.addDialog;
  177 |     await expect(dialog).toBeVisible();
  178 | 
  179 |     log.success('ADM-QUES-003 测试完成');
  180 |   });
  181 | 
  182 |   test('ADM-QUES-003: 添加题目-填写表单并保存', async ({ page }) => {
  183 |     log.info('测试添加题目-填写表单并保存');
  184 |     await questionPage.goto();
  185 |     await page.waitForTimeout(2000);
  186 | 
  187 |     await questionPage.clickAdd();
  188 | 
  189 |     // 填写题目表单
  190 |     const testQuestion = {
  191 |       title: '测试题目-' + Date.now(),
  192 |       content: '这是一个测试题目的内容',
  193 |       category: 'JavaScript',
  194 |       difficulty: '中等',
  195 |     };
  196 | 
  197 |     await questionPage.fillQuestionForm(testQuestion);
  198 |     await questionPage.saveQuestion();
  199 | 
  200 |     await page.waitForTimeout(1000);
  201 | 
  202 |     log.success('ADM-QUES-003 添加题目测试完成');
  203 |   });
  204 | 
  205 |   test('ADM-QUES-005: 编辑题目', async ({ page }) => {
  206 |     log.info('测试编辑题目');
  207 |     await questionPage.goto();
  208 |     await page.waitForTimeout(2000);
  209 | 
  210 |     // 检查是否有可编辑的题目
  211 |     const count = await questionPage.getQuestionCount();
  212 |     if (count > 0) {
  213 |       await questionPage.clickEdit(0);
  214 |       await page.waitForTimeout(500);
  215 | 
  216 |       // 验证编辑弹窗打开
  217 |       const dialog = questionPage.addDialog;
> 218 |       await expect(dialog).toBeVisible();
      |                            ^ Error: expect(locator).toBeVisible() failed
  219 | 
  220 |       // 关闭弹窗
  221 |       await questionPage.cancelBtn.click();
  222 |     }
  223 | 
  224 |     log.success('ADM-QUES-005 测试完成');
  225 |   });
  226 | 
  227 |   test('ADM-QUES-006: 删除题目-取消', async ({ page }) => {
  228 |     log.info('测试删除题目-取消操作');
  229 |     await questionPage.goto();
  230 |     await page.waitForTimeout(2000);
  231 | 
  232 |     // 检查是否有可删除的题目
  233 |     const count = await questionPage.getQuestionCount();
  234 |     if (count > 0) {
  235 |       await questionPage.clickDelete(0);
  236 |       await page.waitForTimeout(500);
  237 | 
  238 |       // 取消删除
  239 |       await questionPage.cancelDelete();
  240 |     }
  241 | 
  242 |     log.success('ADM-QUES-006 测试完成');
  243 |   });
  244 | 
  245 |   test('ADM-QUES-008: 分页功能', async ({ page }) => {
  246 |     log.info('测试分页功能');
  247 |     await questionPage.goto();
  248 |     await page.waitForTimeout(2000);
  249 | 
  250 |     // 检查分页组件
  251 |     const pagination = questionPage.pagination;
  252 |     if (await pagination.isVisible()) {
  253 |       log.info('分页组件可见');
  254 |     }
  255 | 
  256 |     log.success('ADM-QUES-008 测试完成');
  257 |   });
  258 | });
  259 | 
  260 | // ========== 用户管理模块测试 ==========
  261 | 
  262 | test.describe('Admin用户管理模块', () => {
  263 |   let loginPage;
  264 |   let userPage;
  265 | 
  266 |   test.beforeEach(async ({ page }) => {
  267 |     loginPage = new AdminLoginPage(page);
  268 |     userPage = new AdminUserManagementPage(page);
  269 | 
  270 |     // 先登录
  271 |     await page.goto(BASE_URL + '/login');
  272 |     await loginPage.loginWithUniversalCode(TEST_CONFIG.testPhone);
  273 |     await page.waitForTimeout(2000);
  274 |   });
  275 | 
  276 |   test('ADM-USER-001: 用户列表加载', async ({ page }) => {
  277 |     log.info('测试用户列表加载');
  278 |     await userPage.goto();
  279 | 
  280 |     // 等待数据加载
  281 |     await page.waitForTimeout(3000);
  282 | 
  283 |     // 验证表格存在
  284 |     const table = userPage.userTable;
  285 |     await expect(table).toBeVisible();
  286 | 
  287 |     const count = await userPage.getUserCount();
  288 |     log.info(`用户数量: ${count}`);
  289 | 
  290 |     log.success('ADM-USER-001 测试完成');
  291 |   });
  292 | 
  293 |   test('ADM-USER-002: 用户搜索', async ({ page }) => {
  294 |     log.info('测试用户搜索');
  295 |     await userPage.goto();
  296 |     await page.waitForTimeout(2000);
  297 | 
  298 |     // 使用测试账号搜索
  299 |     await userPage.search(TEST_CONFIG.testPhone);
  300 | 
  301 |     await page.waitForTimeout(1000);
  302 | 
  303 |     log.success('ADM-USER-002 测试完成');
  304 |   });
  305 | });
  306 | 
  307 | // ========== 权限测试 ==========
  308 | 
  309 | test.describe('Admin权限测试', () => {
  310 |   test('ADM-AUTH-001: 未授权访问重定向', async ({ page }) => {
  311 |     log.info('测试未授权访问重定向');
  312 | 
  313 |     // 直接访问需要授权的页面
  314 |     await page.goto(BASE_URL + '/dashboard');
  315 |     await page.waitForTimeout(2000);
  316 | 
  317 |     // 验证被重定向到登录页
  318 |     const currentUrl = page.url();
```