// H5页面对象模型

/**
 * H5登录页
 */
class H5LoginPage {
  constructor(page) {
    this.page = page;
    this.phoneInput = page.locator('input[type="tel"]');
    this.codeInput = page.locator('input[type="text"]');
    this.sendCodeBtn = page.locator('.code-btn');
    this.loginBtn = page.locator('.login-btn');
    this.agreedCheckbox = page.locator('.van-checkbox');
  }

  async goto() {
    await this.page.goto('/login');
    await this.page.waitForLoadState('networkidle');
    await this.page.waitForTimeout(2000); // 等待Vue渲染
    // 关闭Vite错误覆盖层（如果存在）
    await this.dismissErrorOverlay();
  }

  async dismissErrorOverlay() {
    try {
      const overlay = this.page.locator('vite-error-overlay');
      if (await overlay.isVisible({ timeout: 1000 })) {
        await this.page.keyboard.press('Escape');
        await this.page.waitForTimeout(500);
      }
    } catch (e) {
      // 没有错误覆盖层
    }
  }

  async login(phone, code) {
    await this.phoneInput.fill(phone);
    await this.sendCodeBtn.click();
    await this.page.waitForTimeout(1000); // 等待验证码发送
    await this.codeInput.fill(code);
    // 必须勾选同意协议
    await this.agreedCheckbox.click();
    await this.loginBtn.click();
    await this.page.waitForLoadState('networkidle');
  }

  async loginWithUniversalCode(phone) {
    await this.login(phone, '000000');
  }
}

/**
 * H5首页
 */
class H5HomePage {
  constructor(page) {
    this.page = page;
    this.banner = page.locator('.banner');
    this.startInterviewBtn = page.locator('.action-card').filter({ hasText: /开始面试/ });
    this.jobCards = page.locator('.job-card');
    this.questionCards = page.locator('.question-card');
  }

  async goto() {
    await this.page.goto('/home');
    await this.page.waitForLoadState('networkidle');
  }

  async clickStartInterview() {
    await this.startInterviewBtn.click();
    await this.page.waitForLoadState('networkidle');
  }

  async getJobCount() {
    return await this.jobCards.count();
  }

  async getQuestionCount() {
    return await this.questionCards.count();
  }
}

/**
 * H5个人中心页
 */
class H5ProfilePage {
  constructor(page) {
    this.page = page;
    this.userInfo = page.locator('.user-card');
    this.nickname = page.locator('.nickname');
    this.statsCard = page.locator('.stats-card');
    this.menuItems = page.locator('.menu-item');
    this.logoutBtn = page.locator('button').filter({ hasText: /退出登录/ });
  }

  async goto() {
    await this.page.goto('/profile');
    await this.page.waitForLoadState('networkidle');
  }

  async getNickname() {
    return await this.nickname.textContent();
  }

  async clickMenuItem(text) {
    await this.menuItems.filter({ hasText: text }).click();
  }

  async logout() {
    await this.logoutBtn.click({ force: true });
    await this.page.waitForLoadState('networkidle');
  }
}

/**
 * H5简历列表页
 */
class H5ResumeListPage {
  constructor(page) {
    this.page = page;
    this.resumeCards = page.locator('.resume-card');
    this.addResumeBtn = page.locator('.add-resume-card');
    this.deleteBtn = page.locator('button').filter({ hasText: /删除/ });
    this.editBtn = page.locator('button').filter({ hasText: /编辑/ });
  }

  async goto() {
    await this.page.goto('/resume');
    await this.page.waitForLoadState('networkidle');
  }

  async getResumeCount() {
    return await this.resumeCards.count();
  }

  async clickAddResume() {
    await this.addResumeBtn.click();
  }

  async clickEditResume(index = 0) {
    await this.editBtn.nth(index).click();
  }
}

/**
 * H5简历编辑页
 */
class H5ResumeEditPage {
  constructor(page) {
    this.page = page;
    this.titleInput = page.locator('input').first();
    this.saveBtn = page.locator('button').filter({ hasText: /保存/ });
    this.backBtn = page.locator('.header-left, .back-btn');
  }

  async goto(resumeId = '') {
    const url = resumeId ? `/resume/edit?id=${resumeId}` : '/resume/edit';
    await this.page.goto(url);
    await this.page.waitForLoadState('networkidle');
  }

  async fillTitle(title) {
    await this.titleInput.fill(title);
  }

  async save() {
    await this.saveBtn.click();
    await this.page.waitForLoadState('networkidle');
  }
}

/**
 * H5面试岗位选择页
 */
class H5JobSelectPage {
  constructor(page) {
    this.page = page;
    this.jobCards = page.locator('.job-card');
    this.nextBtn = page.locator('button').filter({ hasText: /下一步|确定/ });
  }

  async goto() {
    await this.page.goto('/interview/select');
    await this.page.waitForLoadState('networkidle');
  }

  async selectJob(index = 0) {
    await this.jobCards.nth(index).click();
  }

  async clickNext() {
    await this.nextBtn.click();
    await this.page.waitForLoadState('networkidle');
  }
}

/**
 * H5面试配置页
 */
class H5InterviewConfigPage {
  constructor(page) {
    this.page = page;
    this.styleOptions = page.locator('.style-option, .option-card');
    this.modeOptions = page.locator('.mode-option, .mode-card');
    this.startBtn = page.locator('button').filter({ hasText: /开始面试/ });
  }

  async goto() {
    await this.page.goto('/interview/config');
    await this.page.waitForLoadState('networkidle');
  }

  async selectStyle(index = 0) {
    await this.styleOptions.nth(index).click();
  }

  async selectMode(index = 0) {
    await this.modeOptions.nth(index).click();
  }

  async startInterview() {
    await this.startBtn.click();
    await this.page.waitForLoadState('networkidle');
  }
}

/**
 * H5文字面试页
 */
class H5TextInterviewPage {
  constructor(page) {
    this.page = page;
    this.questionArea = page.locator('.question-area, .question-text');
    this.inputArea = page.locator('textarea, input[type="text"]');
    this.sendBtn = page.locator('button').filter({ hasText: /发送/ });
    this.messageList = page.locator('.message-list, .messages');
  }

  async goto(interviewId = '') {
    const url = interviewId ? `/interview/text/${interviewId}` : '/interview/text';
    await this.page.goto(url);
    await this.page.waitForLoadState('networkidle');
  }

  async sendMessage(message) {
    await this.inputArea.fill(message);
    await this.sendBtn.click();
    await this.page.waitForTimeout(1000); // 等待AI响应
  }
}

/**
 * H5面试报告页
 */
class H5InterviewReportPage {
  constructor(page) {
    this.page = page;
    this.scoreDisplay = page.locator('.score-value, .total-score');
    this.gradeTag = page.locator('.grade-tag, .van-tag');
    this.retryBtn = page.locator('button').filter({ hasText: /再来一次/ });
    this.shareBtn = page.locator('button').filter({ hasText: /分享/ });
  }

  async goto(interviewId = '') {
    const url = interviewId ? `/interview/report/${interviewId}` : '/interview/report';
    await this.page.goto(url);
    await this.page.waitForLoadState('networkidle');
  }

  async getScore() {
    return await this.scoreDisplay.textContent();
  }

  async clickRetry() {
    await this.retryBtn.click();
    await this.page.waitForLoadState('networkidle');
  }
}

module.exports = {
  H5LoginPage,
  H5HomePage,
  H5ProfilePage,
  H5ResumeListPage,
  H5ResumeEditPage,
  H5JobSelectPage,
  H5InterviewConfigPage,
  H5TextInterviewPage,
  H5InterviewReportPage,
};
