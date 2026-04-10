# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: admin.spec.js >> Admin题库管理模块 >> ADM-QUES-003: 添加题目-填写表单并保存
- Location: tests/admin.spec.js:182:3

# Error details

```
Error: locator.isVisible: Error: strict mode violation: locator('textarea') resolved to 3 elements:
    1) <textarea rows="3" tabindex="0" autocomplete="off" id="el-id-2704-33" placeholder="请输入题目内容" class="el-textarea__inner"></textarea> aka getByRole('textbox', { name: '* 题目内容' })
    2) <textarea rows="4" tabindex="0" autocomplete="off" id="el-id-2704-38" placeholder="请输入参考答案" class="el-textarea__inner"></textarea> aka getByRole('textbox', { name: '参考答案' })
    3) <textarea rows="3" tabindex="0" autocomplete="off" id="el-id-2704-39" placeholder="请输入题目解析" class="el-textarea__inner"></textarea> aka getByRole('textbox', { name: '解析' })

Call log:
    - checking visibility of locator('textarea')

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
      - generic [ref=e54]:
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
        - dialog "添加题目" [ref=e483]:
          - generic [ref=e484]:
            - generic [ref=e485]:
              - heading "添加题目" [level=2] [ref=e486]
              - button "Close this dialog" [ref=e487] [cursor=pointer]:
                - img [ref=e489]
            - generic [ref=e492]:
              - generic [ref=e493]:
                - generic [ref=e494]: "* 题目标题"
                - textbox "* 题目标题" [active] [ref=e498]:
                  - /placeholder: 请输入题目标题
                  - text: 测试题目-1775781828187
              - generic [ref=e499]:
                - generic [ref=e500]: "* 题目内容"
                - textbox "* 题目内容" [ref=e503]:
                  - /placeholder: 请输入题目内容
              - generic [ref=e504]:
                - generic [ref=e505]: "* 题目类型"
                - generic [ref=e508] [cursor=pointer]:
                  - generic:
                    - combobox "* 题目类型" [ref=e510]
                    - generic [ref=e511]: 请选择题目类型
                  - img [ref=e514]
              - generic [ref=e516]:
                - generic [ref=e517]: "* 难度"
                - generic [ref=e520] [cursor=pointer]:
                  - generic:
                    - combobox "* 难度" [ref=e522]
                    - generic [ref=e523]: 请选择难度
                  - img [ref=e526]
              - generic [ref=e528]:
                - generic [ref=e529]: 分类
                - textbox "分类" [ref=e533]:
                  - /placeholder: 请输入分类
              - generic [ref=e534]:
                - generic [ref=e535]: 标签
                - textbox "标签" [ref=e539]:
                  - /placeholder: 请输入标签，多个用逗号分隔
              - generic [ref=e540]:
                - generic [ref=e541]: 参考答案
                - textbox "参考答案" [ref=e544]:
                  - /placeholder: 请输入参考答案
              - generic [ref=e545]:
                - generic [ref=e546]: 解析
                - textbox "解析" [ref=e549]:
                  - /placeholder: 请输入题目解析
            - generic [ref=e550]:
              - button "取消" [ref=e551] [cursor=pointer]:
                - generic [ref=e552]: 取消
              - button "确定" [ref=e553] [cursor=pointer]:
                - generic [ref=e554]: 确定
```

# Test source

```ts
  10  |     this.page = page;
  11  |     this.baseUrl = ADMIN_BASE_URL;
  12  |     this.phoneInput = page.locator('input[type="text"], input[type="tel"]').first();
  13  |     this.codeInput = page.locator('input[type="password"], input').nth(1);
  14  |     this.loginBtn = page.locator('button').filter({ hasText: /登录|登录/ }).first();
  15  |   }
  16  | 
  17  |   async goto() {
  18  |     await this.page.goto(this.baseUrl + '/login');
  19  |     await this.page.waitForLoadState('networkidle');
  20  |   }
  21  | 
  22  |   async login(phone, code) {
  23  |     await this.phoneInput.fill(phone);
  24  |     await this.codeInput.fill(code);
  25  |     await this.loginBtn.click();
  26  |     await this.page.waitForLoadState('networkidle');
  27  |   }
  28  | 
  29  |   async loginWithUniversalCode(phone) {
  30  |     await this.login(phone, '000000');
  31  |   }
  32  | }
  33  | 
  34  | /**
  35  |  * Admin仪表盘页
  36  |  */
  37  | class AdminDashboardPage {
  38  |   constructor(page) {
  39  |     this.page = page;
  40  |     this.baseUrl = ADMIN_BASE_URL;
  41  |     this.statCards = page.locator('.stat-card');
  42  |     this.totalUsers = page.locator('.stat-value').first();
  43  |     this.totalResumes = page.locator('.stat-value').nth(1);
  44  |     this.totalInterviews = page.locator('.stat-value').nth(2);
  45  |     this.totalQuestions = page.locator('.stat-value').nth(3);
  46  |     this.recentActivitiesTable = page.locator('.el-table');
  47  |     this.refreshBtn = page.locator('.chart-card button').filter({ hasText: /刷新/ });
  48  |   }
  49  | 
  50  |   async goto() {
  51  |     await this.page.goto(this.baseUrl + '/dashboard');
  52  |     await this.page.waitForLoadState('networkidle');
  53  |   }
  54  | 
  55  |   async getStats() {
  56  |     const stats = {
  57  |       totalUsers: await this.totalUsers.textContent(),
  58  |       totalResumes: await this.totalResumes.textContent(),
  59  |       totalInterviews: await this.totalInterviews.textContent(),
  60  |       totalQuestions: await this.totalQuestions.textContent(),
  61  |     };
  62  |     return stats;
  63  |   }
  64  | 
  65  |   async clickRefresh() {
  66  |     await this.refreshBtn.first().click();
  67  |     await this.page.waitForTimeout(1000);
  68  |   }
  69  | }
  70  | 
  71  | /**
  72  |  * Admin题库管理页
  73  |  */
  74  | class AdminQuestionManagementPage {
  75  |   constructor(page) {
  76  |     this.page = page;
  77  |     this.baseUrl = ADMIN_BASE_URL;
  78  |     this.questionTable = page.locator('.el-table');
  79  |     this.questionRows = page.locator('.el-table__row');
  80  |     this.addBtn = page.locator('.el-button').filter({ hasText: /添加题目/ });
  81  |     this.editBtn = page.locator('.el-button--link').filter({ hasText: /编辑/ });
  82  |     this.deleteBtn = page.locator('.el-button--link').filter({ hasText: /删除/ });
  83  |     this.pagination = page.locator('.el-pagination');
  84  |     this.addDialog = page.locator('.el-dialog');
  85  |     this.titleInput = page.locator('input[placeholder*="题"], input[placeholder*="目"]');
  86  |     this.contentInput = page.locator('textarea');
  87  |     this.categorySelect = page.locator('.el-select');
  88  |     this.difficultySelect = page.locator('.el-select').nth(1);
  89  |     this.saveBtn = page.locator('.el-button--primary').filter({ hasText: /确定/ });
  90  |     this.cancelBtn = page.locator('.el-dialog__footer .el-button').filter({ hasText: /取消/ });
  91  |     this.deleteConfirmBtn = page.locator('.el-message-box__btns .el-button');
  92  |   }
  93  | 
  94  |   async goto() {
  95  |     await this.page.goto(this.baseUrl + '/questions');
  96  |     await this.page.waitForLoadState('networkidle');
  97  |   }
  98  | 
  99  |   async getQuestionCount() {
  100 |     return await this.questionRows.count();
  101 |   }
  102 | 
  103 |   async clickAdd() {
  104 |     await this.addBtn.click({ force: true });
  105 |     await this.page.waitForTimeout(500);
  106 |   }
  107 | 
  108 |   async fillQuestionForm(question) {
  109 |     await this.titleInput.fill(question.title || question.content);
> 110 |     if (await this.contentInput.isVisible()) {
      |                                 ^ Error: locator.isVisible: Error: strict mode violation: locator('textarea') resolved to 3 elements:
  111 |       await this.contentInput.fill(question.content);
  112 |     }
  113 |     // 选择分类
  114 |     if (question.category) {
  115 |       await this.categorySelect.click();
  116 |       await this.page.locator(`.el-option`).filter({ hasText: question.category }).click();
  117 |     }
  118 |     // 选择难度
  119 |     if (question.difficulty) {
  120 |       await this.difficultySelect.click();
  121 |       await this.page.locator(`.el-option`).filter({ hasText: question.difficulty }).click();
  122 |     }
  123 |   }
  124 | 
  125 |   async saveQuestion() {
  126 |     // 使用 JavaScript 点击避免 Element Plus 样式问题
  127 |     await this.page.evaluate(() => {
  128 |       const btn = document.querySelector('.el-dialog__footer .el-button--primary');
  129 |       if (btn) btn.click();
  130 |     });
  131 |     await this.page.waitForTimeout(1000);
  132 |   }
  133 | 
  134 |   async clickEdit(index = 0) {
  135 |     // 使用 JavaScript 点击避免 Element Plus 表格样式问题
  136 |     await this.page.evaluate((i) => {
  137 |       const buttons = document.querySelectorAll('.el-table__row');
  138 |       if (buttons[i]) {
  139 |         const editBtn = buttons[i].querySelector('.el-button--link');
  140 |         if (editBtn) editBtn.click();
  141 |       }
  142 |     }, index);
  143 |     await this.page.waitForTimeout(500);
  144 |   }
  145 | 
  146 |   async clickDelete(index = 0) {
  147 |     // 使用 JavaScript 点击避免 Element Plus 表格样式问题
  148 |     await this.page.evaluate((i) => {
  149 |       const buttons = document.querySelectorAll('.el-table__row');
  150 |       if (buttons[i]) {
  151 |         const deleteBtn = buttons[i].querySelectorAll('.el-button--link')[1]; // 第二个是删除按钮
  152 |         if (deleteBtn) deleteBtn.click();
  153 |       }
  154 |     }, index);
  155 |     await this.page.waitForTimeout(500);
  156 |   }
  157 | 
  158 |   async confirmDelete() {
  159 |     await this.page.locator('.el-message-box__btns .el-button--primary').click();
  160 |     await this.page.waitForTimeout(1000);
  161 |   }
  162 | 
  163 |   async cancelDelete() {
  164 |     // 使用 JavaScript 点击避免 Element Plus MessageBox 样式问题
  165 |     await this.page.evaluate(() => {
  166 |       const buttons = document.querySelectorAll('.el-message-box__btns .el-button');
  167 |       // 取消按钮是第一个
  168 |       if (buttons[0]) buttons[0].click();
  169 |     });
  170 |     await this.page.waitForTimeout(1000);
  171 |   }
  172 | }
  173 | 
  174 | /**
  175 |  * Admin用户管理页
  176 |  */
  177 | class AdminUserManagementPage {
  178 |   constructor(page) {
  179 |     this.page = page;
  180 |     this.baseUrl = ADMIN_BASE_URL;
  181 |     this.userTable = page.locator('.el-table');
  182 |     this.userRows = page.locator('.el-table__row');
  183 |     this.searchInput = page.locator('input[placeholder*="搜索"]');
  184 |     this.searchBtn = page.locator('.el-input-group__append .el-button');
  185 |   }
  186 | 
  187 |   async goto() {
  188 |     await this.page.goto(this.baseUrl + '/users');
  189 |     await this.page.waitForLoadState('networkidle');
  190 |   }
  191 | 
  192 |   async getUserCount() {
  193 |     return await this.userRows.count();
  194 |   }
  195 | 
  196 |   async search(keyword) {
  197 |     await this.searchInput.fill(keyword);
  198 |     await this.searchBtn.click();
  199 |     await this.page.waitForTimeout(1000);
  200 |   }
  201 | }
  202 | 
  203 | /**
  204 |  * Admin简历管理页
  205 |  */
  206 | class AdminResumeManagementPage {
  207 |   constructor(page) {
  208 |     this.page = page;
  209 |     this.baseUrl = ADMIN_BASE_URL;
  210 |     this.resumeTable = page.locator('.el-table');
```