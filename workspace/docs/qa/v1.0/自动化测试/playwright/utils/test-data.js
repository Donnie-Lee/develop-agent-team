// 测试配置和辅助函数

const TEST_CONFIG = {
  // 测试账号
  testPhone: '13800138000',
  verifyCode: '000000', // 万能验证码

  // H5前端地址
  h5BaseUrl: 'http://localhost:5173',

  // Admin管理端地址
  adminBaseUrl: 'http://localhost:5174',

  // 超时配置
  timeout: {
    short: 5000,
    medium: 10000,
    long: 30000,
  },

  // 等待配置
  waitForTimeout: 1000,
};

// 辅助函数：等待指定时间
const wait = async (ms) => {
  return new Promise(resolve => setTimeout(resolve, ms));
};

// 辅助函数：截图保存
const takeScreenshot = async (page, name) => {
  const path = `screenshots/${name}-${Date.now()}.png`;
  await page.screenshot({ path, fullPage: true });
  console.log(`Screenshot saved: ${path}`);
  return path;
};

// 辅助函数：控制台日志
const log = {
  info: (msg) => console.log(`[INFO] ${new Date().toISOString()} - ${msg}`),
  success: (msg) => console.log(`[SUCCESS] ${new Date().toISOString()} - ${msg}`),
  error: (msg) => console.log(`[ERROR] ${new Date().toISOString()} - ${msg}`),
  warn: (msg) => console.log(`[WARN] ${new Date().toISOString()} - ${msg}`),
};

module.exports = {
  TEST_CONFIG,
  wait,
  takeScreenshot,
  log,
};
