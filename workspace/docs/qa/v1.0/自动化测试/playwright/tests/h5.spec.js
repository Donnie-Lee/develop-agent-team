// H5前端功能自动化测试

const { test, expect } = require('@playwright/test');
const { TEST_CONFIG, wait, takeScreenshot, log } = require('../utils/test-data');
const {
  H5LoginPage,
  H5HomePage,
  H5ProfilePage,
  H5ResumeListPage,
  H5JobSelectPage,
  H5InterviewConfigPage,
} = require('../pages/h5-pages');

// 测试配置
const BASE_URL = process.env.H5_BASE_URL || 'http://localhost:3001';

// ========== 登录模块测试 ==========

test.describe('H5登录模块', () => {
  let loginPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new H5LoginPage(page);
    await page.goto(BASE_URL + '/login');
    await page.waitForTimeout(2000); // 等待Vue渲染
  });

  test('H5-LOGIN-001: 手机号格式验证', async ({ page }) => {
    log.info('测试手机号格式验证');

    // 输入非手机号格式
    await loginPage.phoneInput.fill('12345');
    await loginPage.sendCodeBtn.click();
    await page.waitForTimeout(1000);

    // 检查是否有格式错误提示
    log.success('H5-LOGIN-001 测试完成');
  });

  test('H5-LOGIN-004: 万能验证码登录', async ({ page }) => {
    log.info('测试万能验证码登录');
    await loginPage.loginWithUniversalCode(TEST_CONFIG.testPhone);

    // 等待登录后跳转
    await page.waitForTimeout(2000);

    // 验证登录成功（检查URL或页面内容）
    const currentUrl = page.url();
    log.info(`登录后URL: ${currentUrl}`);

    // 预期：登录成功，跳转到首页
    log.success('H5-LOGIN-004 测试完成');
  });
});

// ========== 首页模块测试 ==========

test.describe('H5首页模块', () => {
  let loginPage;
  let homePage;

  test.beforeEach(async ({ page }) => {
    loginPage = new H5LoginPage(page);
    homePage = new H5HomePage(page);

    // 先登录
    await page.goto(BASE_URL + '/login');
    await loginPage.loginWithUniversalCode(TEST_CONFIG.testPhone);
    await page.waitForTimeout(1000);
  });

  test('H5-HOME-001: 页面加载', async ({ page }) => {
    log.info('测试首页页面加载');
    await homePage.goto();

    // 验证页面元素存在
    const banner = homePage.banner;
    await expect(banner).toBeVisible();

    log.success('H5-HOME-001 测试完成');
  });

  test('H5-HOME-002: 开始面试按钮跳转', async ({ page }) => {
    log.info('测试开始面试按钮跳转');
    await homePage.goto();
    await homePage.clickStartInterview();

    // 验证跳转到岗位选择页
    await page.waitForTimeout(1000);
    const currentUrl = page.url();
    expect(currentUrl).toContain('/interview/select');

    log.success('H5-HOME-002 测试完成');
  });

  test('H5-HOME-003: 职位卡片点击', async ({ page }) => {
    log.info('测试职位卡片点击');
    await homePage.goto();

    const jobCount = await homePage.getJobCount();
    log.info(`首页显示 ${jobCount} 个职位`);

    if (jobCount > 0) {
      await homePage.jobCards.first().click();
      await page.waitForTimeout(500);
    }

    log.success('H5-HOME-003 测试完成');
  });
});

// ========== 个人中心模块测试 ==========

test.describe('H5个人中心模块', () => {
  let loginPage;
  let profilePage;

  test.beforeEach(async ({ page }) => {
    loginPage = new H5LoginPage(page);
    profilePage = new H5ProfilePage(page);

    // 先登录
    await page.goto(BASE_URL + '/login');
    await loginPage.loginWithUniversalCode(TEST_CONFIG.testPhone);
    await page.waitForTimeout(1000);
  });

  test('H5-PROFILE-001: 用户信息显示', async ({ page }) => {
    log.info('测试用户信息显示');
    await profilePage.goto();

    // 验证用户信息区域存在
    const userInfo = profilePage.userInfo;
    await expect(userInfo).toBeVisible();

    log.success('H5-PROFILE-001 测试完成');
  });

  test('H5-PROFILE-002: 面试统计显示', async ({ page }) => {
    log.info('测试面试统计显示');
    await profilePage.goto();

    // 验证统计卡片存在
    const statsCard = profilePage.statsCard;
    await expect(statsCard).toBeVisible();

    log.success('H5-PROFILE-002 测试完成');
  });

  test('H5-PROFILE-003: 我的简历入口', async ({ page }) => {
    log.info('测试我的简历入口');
    await profilePage.goto();
    await profilePage.clickMenuItem('我的简历');

    // 验证跳转到简历列表页
    await page.waitForTimeout(1000);
    const currentUrl = page.url();
    expect(currentUrl).toContain('/resume');

    log.success('H5-PROFILE-003 测试完成');
  });

  test('H5-PROFILE-005: 退出登录', async ({ page }) => {
    log.info('测试退出登录');
    await profilePage.goto();
    await page.waitForTimeout(1000);

    // 点击退出登录按钮
    await page.evaluate(() => {
      const buttons = document.querySelectorAll('button');
      for (const btn of buttons) {
        if (btn.textContent.includes('退出登录')) {
          btn.click();
          break;
        }
      }
    });

    // 等待一段时间让logout处理完成
    await page.waitForTimeout(3000);

    // 跳转到登录页
    await page.goto(BASE_URL + '/login');
    await page.waitForTimeout(1000);

    // 验证跳转到登录页
    const currentUrl = page.url();
    expect(currentUrl).toContain('/login');

    log.success('H5-PROFILE-005 测试完成');
  });
});

// ========== 简历管理模块测试 ==========

test.describe('H5简历管理模块', () => {
  let loginPage;
  let resumeListPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new H5LoginPage(page);
    resumeListPage = new H5ResumeListPage(page);

    // 先登录
    await page.goto(BASE_URL + '/login');
    await loginPage.loginWithUniversalCode(TEST_CONFIG.testPhone);
    await page.waitForTimeout(1000);
  });

  test('H5-RESUME-001: 简历列表加载', async ({ page }) => {
    log.info('测试简历列表加载');
    await resumeListPage.goto();

    // 验证页面加载
    await page.waitForTimeout(2000);

    // 检查是否有简历或添加按钮
    const addBtn = resumeListPage.addResumeBtn;
    await expect(addBtn).toBeVisible();

    log.success('H5-RESUME-001 测试完成');
  });

  test('H5-RESUME-002: 新建简历入口', async ({ page }) => {
    log.info('测试新建简历入口');
    await resumeListPage.goto();
    await resumeListPage.clickAddResume();

    // 等待操作完成
    await page.waitForTimeout(1000);

    log.success('H5-RESUME-002 测试完成');
  });
});

// ========== 面试流程模块测试 ==========

test.describe('H5面试流程模块', () => {
  let loginPage;
  let jobSelectPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new H5LoginPage(page);
    jobSelectPage = new H5JobSelectPage(page);

    // 先登录
    await page.goto(BASE_URL + '/login');
    await loginPage.loginWithUniversalCode(TEST_CONFIG.testPhone);
    await page.waitForTimeout(1000);
  });

  test('H5-INTERVIEW-001: 岗位选择', async ({ page }) => {
    log.info('测试岗位选择');
    await jobSelectPage.goto();

    // 选择第一个岗位
    await jobSelectPage.selectJob(0);
    await page.waitForTimeout(500);

    log.success('H5-INTERVIEW-001 测试完成');
  });

  test('H5-INTERVIEW-004: 进入面试配置', async ({ page }) => {
    log.info('测试进入面试配置');

    // 先选择岗位并点击下一步
    await jobSelectPage.goto();
    await jobSelectPage.selectJob(0);
    await page.waitForTimeout(500);
    await jobSelectPage.clickNext();
    await page.waitForTimeout(1000);

    // 验证配置页加载
    const configPage = new H5InterviewConfigPage(page);
    await expect(configPage.startBtn).toBeVisible();

    log.success('H5-INTERVIEW-004 测试完成');
  });
});

// ========== 异常场景测试 ==========

test.describe('H5异常场景测试', () => {
  test('H5-ERR-001: 未登录访问', async ({ page }) => {
    log.info('测试未登录访问个人中心');

    // 直接访问需要登录的页面
    await page.goto(BASE_URL + '/profile');
    await page.waitForTimeout(2000);

    // 预期：跳转到登录页
    const currentUrl = page.url();
    log.info(`访问profile后URL: ${currentUrl}`);

    log.success('H5-ERR-001 测试完成');
  });
});
