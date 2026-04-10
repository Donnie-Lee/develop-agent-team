// Admin管理端页面对象模型

const ADMIN_BASE_URL = process.env.ADMIN_BASE_URL || 'http://localhost:3000';

/**
 * Admin登录页
 */
class AdminLoginPage {
  constructor(page) {
    this.page = page;
    this.baseUrl = ADMIN_BASE_URL;
    this.phoneInput = page.locator('input[type="text"], input[type="tel"]').first();
    this.codeInput = page.locator('input[type="password"], input').nth(1);
    this.loginBtn = page.locator('button').filter({ hasText: /登录|登录/ }).first();
  }

  async goto() {
    await this.page.goto(this.baseUrl + '/login');
    await this.page.waitForLoadState('networkidle');
  }

  async login(phone, code) {
    await this.phoneInput.fill(phone);
    await this.codeInput.fill(code);
    await this.loginBtn.click();
    await this.page.waitForLoadState('networkidle');
  }

  async loginWithUniversalCode(phone) {
    await this.login(phone, '000000');
  }
}

/**
 * Admin仪表盘页
 */
class AdminDashboardPage {
  constructor(page) {
    this.page = page;
    this.baseUrl = ADMIN_BASE_URL;
    this.statCards = page.locator('.stat-card');
    this.totalUsers = page.locator('.stat-value').first();
    this.totalResumes = page.locator('.stat-value').nth(1);
    this.totalInterviews = page.locator('.stat-value').nth(2);
    this.totalQuestions = page.locator('.stat-value').nth(3);
    this.recentActivitiesTable = page.locator('.el-table');
    this.refreshBtn = page.locator('.chart-card button').filter({ hasText: /刷新/ });
  }

  async goto() {
    await this.page.goto(this.baseUrl + '/dashboard');
    await this.page.waitForLoadState('networkidle');
  }

  async getStats() {
    const stats = {
      totalUsers: await this.totalUsers.textContent(),
      totalResumes: await this.totalResumes.textContent(),
      totalInterviews: await this.totalInterviews.textContent(),
      totalQuestions: await this.totalQuestions.textContent(),
    };
    return stats;
  }

  async clickRefresh() {
    await this.refreshBtn.first().click();
    await this.page.waitForTimeout(1000);
  }
}

/**
 * Admin题库管理页
 */
class AdminQuestionManagementPage {
  constructor(page) {
    this.page = page;
    this.baseUrl = ADMIN_BASE_URL;
    this.questionTable = page.locator('.el-table');
    this.questionRows = page.locator('.el-table__row');
    this.addBtn = page.locator('.el-button').filter({ hasText: /添加题目/ });
    this.editBtn = page.locator('.el-button--link').filter({ hasText: /编辑/ });
    this.deleteBtn = page.locator('.el-button--link').filter({ hasText: /删除/ });
    this.pagination = page.locator('.el-pagination');
    this.addDialog = page.locator('.el-dialog');
    this.titleInput = page.locator('input[placeholder*="题"], input[placeholder*="目"]');
    this.contentInput = page.locator('textarea').first();
    this.categorySelect = page.locator('.el-dialog .el-select').first();
    this.difficultySelect = page.locator('.el-dialog .el-select').nth(1);
    this.saveBtn = page.locator('.el-button--primary').filter({ hasText: /确定/ });
    this.cancelBtn = page.locator('.el-dialog__footer .el-button').filter({ hasText: /取消/ });
    this.deleteConfirmBtn = page.locator('.el-message-box__btns .el-button');
  }

  async goto() {
    await this.page.goto(this.baseUrl + '/questions');
    await this.page.waitForLoadState('networkidle');
  }

  async getQuestionCount() {
    return await this.questionRows.count();
  }

  async clickAdd() {
    await this.addBtn.click({ force: true });
    await this.page.waitForTimeout(500);
  }

  async fillQuestionForm(question) {
    await this.titleInput.fill(question.title || question.content);
    if (await this.contentInput.isVisible()) {
      await this.contentInput.fill(question.content);
    }
    // 选择分类
    if (question.category) {
      await this.categorySelect.click();
      await this.page.waitForTimeout(500);
      // 使用JavaScript点击下拉选项
      await this.page.evaluate((cat) => {
        const options = document.querySelectorAll('.el-select-dropdown__item, .el-option');
        for (const opt of options) {
          if (opt.textContent.includes(cat)) {
            opt.click();
            break;
          }
        }
      }, question.category);
      // 点击其他地方关闭下拉框
      await this.page.locator('.el-dialog').click({ position: { x: 10, y: 10 } });
      await this.page.waitForTimeout(300);
    }
    // 选择难度
    if (question.difficulty) {
      await this.difficultySelect.click();
      await this.page.waitForTimeout(500);
      // 使用JavaScript点击下拉选项
      await this.page.evaluate((diff) => {
        const options = document.querySelectorAll('.el-select-dropdown__item, .el-option');
        for (const opt of options) {
          if (opt.textContent.includes(diff)) {
            opt.click();
            break;
          }
        }
      }, question.difficulty);
      // 点击其他地方关闭下拉框
      await this.page.locator('.el-dialog').click({ position: { x: 10, y: 10 } });
      await this.page.waitForTimeout(300);
    }
  }

  async saveQuestion() {
    // 使用 JavaScript 点击避免 Element Plus 样式问题
    await this.page.evaluate(() => {
      const btn = document.querySelector('.el-dialog__footer .el-button--primary');
      if (btn) btn.click();
    });
    await this.page.waitForTimeout(1000);
  }

  async clickEdit(index = 0) {
    // 找到所有"编辑"按钮 - 使用原生button元素
    const editButtons = this.page.locator('.el-table button').filter({ hasText: '编辑' });
    const count = await editButtons.count();
    if (count > 0) {
      await editButtons.first().click({ force: true });
    }
    await this.page.waitForTimeout(500);
  }

  async clickDelete(index = 0) {
    // 使用 JavaScript 点击避免 Element Plus 表格样式问题
    await this.page.evaluate((i) => {
      const buttons = document.querySelectorAll('.el-table__row');
      if (buttons[i]) {
        const deleteBtn = buttons[i].querySelectorAll('.el-button--link')[1]; // 第二个是删除按钮
        if (deleteBtn) deleteBtn.click();
      }
    }, index);
    await this.page.waitForTimeout(500);
  }

  async confirmDelete() {
    await this.page.locator('.el-message-box__btns .el-button--primary').click();
    await this.page.waitForTimeout(1000);
  }

  async cancelDelete() {
    // 使用 JavaScript 点击避免 Element Plus MessageBox 样式问题
    await this.page.evaluate(() => {
      const buttons = document.querySelectorAll('.el-message-box__btns .el-button');
      // 取消按钮是第一个
      if (buttons[0]) buttons[0].click();
    });
    await this.page.waitForTimeout(1000);
  }
}

/**
 * Admin用户管理页
 */
class AdminUserManagementPage {
  constructor(page) {
    this.page = page;
    this.baseUrl = ADMIN_BASE_URL;
    this.userTable = page.locator('.el-table');
    this.userRows = page.locator('.el-table__row');
    this.searchInput = page.locator('input[placeholder*="搜索"]');
    this.searchBtn = page.locator('.el-input-group__append .el-button');
  }

  async goto() {
    await this.page.goto(this.baseUrl + '/users');
    await this.page.waitForLoadState('networkidle');
  }

  async getUserCount() {
    return await this.userRows.count();
  }

  async search(keyword) {
    await this.searchInput.fill(keyword);
    await this.searchBtn.click();
    await this.page.waitForTimeout(1000);
  }
}

/**
 * Admin简历管理页
 */
class AdminResumeManagementPage {
  constructor(page) {
    this.page = page;
    this.baseUrl = ADMIN_BASE_URL;
    this.resumeTable = page.locator('.el-table');
    this.resumeRows = page.locator('.el-table__row');
    this.viewBtn = page.locator('button').filter({ hasText: /查看/ }).first();
  }

  async goto() {
    await this.page.goto(this.baseUrl + '/resume-management');
    await this.page.waitForLoadState('networkidle');
  }

  async getResumeCount() {
    return await this.resumeRows.count();
  }

  async clickView(index = 0) {
    await this.viewBtn.nth(index).click();
    await this.page.waitForTimeout(500);
  }
}

/**
 * Admin面试管理页
 */
class AdminInterviewManagementPage {
  constructor(page) {
    this.page = page;
    this.baseUrl = ADMIN_BASE_URL;
    this.interviewTable = page.locator('.el-table');
    this.interviewRows = page.locator('.el-table__row');
    this.viewBtn = page.locator('button').filter({ hasText: /查看/ }).first();
  }

  async goto() {
    await this.page.goto(this.baseUrl + '/interview-management');
    await this.page.waitForLoadState('networkidle');
  }

  async getInterviewCount() {
    return await this.interviewRows.count();
  }

  async clickView(index = 0) {
    await this.viewBtn.nth(index).click();
    await this.page.waitForTimeout(500);
  }
}

/**
 * Admin系统设置页
 */
class AdminSettingsPage {
  constructor(page) {
    this.page = page;
    this.baseUrl = ADMIN_BASE_URL;
    this.saveBtn = page.locator('button').filter({ hasText: /保存/ });
  }

  async goto() {
    await this.page.goto(this.baseUrl + '/settings');
    await this.page.waitForLoadState('networkidle');
  }

  async saveSettings() {
    await this.saveBtn.click();
    await this.page.waitForTimeout(1000);
  }
}

module.exports = {
  AdminLoginPage,
  AdminDashboardPage,
  AdminQuestionManagementPage,
  AdminUserManagementPage,
  AdminResumeManagementPage,
  AdminInterviewManagementPage,
  AdminSettingsPage,
};
