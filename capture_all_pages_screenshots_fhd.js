const fs = require('fs');
const path = require('path');
const { chromium } = require('/home/sagar/.vscode/extensions/oderwat.indent-rainbow-8.3.1/node_modules/playwright');

const WEB_BASE = process.env.WEB_BASE || 'http://localhost:3000';
const API_BASE = process.env.API_BASE || 'http://localhost:8080/api';
const CHROME_PATH = '/home/sagar/.cache/ms-playwright/chromium-1208/chrome-linux64/chrome';

const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
const OUT_DIR = path.join('/home/sagar/project/restorent_management_system', 'screenshots', `all-pages-fhd-${timestamp}`);

async function api(pathname, options = {}) {
  const url = `${API_BASE}${pathname}`;
  const response = await fetch(url, options);
  let data = null;
  try {
    data = await response.json();
  } catch (_) {
    data = null;
  }
  return { ok: response.ok, status: response.status, data, url };
}

async function prepareAuthData() {
  const ts = Date.now();
  const email = `demo.screenshots.${ts}@mail.com`;
  const password = 'Demo@1234';

  const registerPayload = {
    name: 'Demo Screenshot User',
    email,
    contactNumber: '9' + String(ts).slice(-9),
    address: 'Screenshot Street',
    password,
    confirmPassword: password,
  };

  const register = await api('/customers/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(registerPayload),
  });

  if (!register.ok) {
    throw new Error(`Customer register failed (${register.status}): ${JSON.stringify(register.data)}`);
  }

  const login = await api('/customers/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password }),
  });

  if (!login.ok || !login.data?.data) {
    throw new Error(`Customer login failed (${login.status}): ${JSON.stringify(login.data)}`);
  }

  const customerSession = login.data.data;

  const adminLogin = await api(`/employee/login?email=${encodeURIComponent('admin@tfc.com')}&password=${encodeURIComponent('admin')}`, {
    method: 'POST',
  });

  const adminSession = adminLogin.data?.data || {
    employeeId: 1,
    name: 'Admin',
    email: 'admin@tfc.com',
    role: 'ADMIN',
  };

  return {
    customerSession,
    adminSession,
    customerCreds: { email, password },
    adminLoginOk: adminLogin.ok,
  };
}

async function createContext(browser, storage) {
  const context = await browser.newContext({
    viewport: { width: 1920, height: 1080 },
  });

  if (storage?.customer) {
    await context.addInitScript((customer) => {
      localStorage.setItem('customer', JSON.stringify(customer));
    }, storage.customer);
  }

  if (storage?.admin) {
    await context.addInitScript((admin) => {
      localStorage.setItem('admin', JSON.stringify(admin));
    }, storage.admin);
  }

  return context;
}

async function capture(context, route, fileName, waitMs = 2200) {
  const page = await context.newPage();
  const target = `${WEB_BASE}${route}`;
  await page.goto(target, { waitUntil: 'domcontentloaded' });
  await page.waitForTimeout(waitMs);
  const outPath = path.join(OUT_DIR, fileName);
  await page.screenshot({ path: outPath, fullPage: false });
  await page.close();
  return { route, fileName, outPath };
}

async function main() {
  fs.mkdirSync(OUT_DIR, { recursive: true });

  const auth = await prepareAuthData();

  const browser = await chromium.launch({
    headless: true,
    executablePath: CHROME_PATH,
    args: ['--no-sandbox', '--disable-dev-shm-usage'],
  });

  const guestContext = await createContext(browser, {});
  const customerContext = await createContext(browser, { customer: auth.customerSession });
  const adminContext = await createContext(browser, { admin: auth.adminSession });

  const captures = [];

  const guestRoutes = [
    ['/', '01-home.png', 2400],
    ['/menu', '02-menu.png', 2800],
    ['/dine', '03-dine-guest.png', 3000],
    ['/sign-in', '04-customer-login.png', 2000],
    ['/sign-up', '05-signup-email.png', 2000],
    ['/sign-up/otp', '06-signup-otp.png', 2000],
    ['/new-customer', '07-new-customer.png', 2000],
    ['/cart', '08-cart-guest.png', 2200],
    ['/admin/login', '09-admin-login.png', 2000],
  ];

  const customerRoutes = [
    ['/', '10-home-customer.png', 2400],
    ['/orders', '11-orders-customer.png', 3200],
    ['/dine', '12-dine-customer.png', 3200],
    ['/cart', '13-cart-customer.png', 2400],
  ];

  const adminRoutes = [
    ['/admin/dashboard', '14-admin-dashboard.png', 3200],
    ['/admin/add-food-item', '15-admin-add-food-item.png', 3000],
    ['/admin/remove-food-item', '16-admin-remove-food-item.png', 3000],
    ['/admin/categories', '17-admin-categories.png', 3000],
    ['/admin/payment-settings', '18-admin-payment-settings.png', 3200],
    ['/admin/customers', '19-admin-customers.png', 3500],
    ['/admin/dine', '20-admin-dine.png', 3500],
  ];

  for (const [route, fileName, waitMs] of guestRoutes) {
    captures.push(await capture(guestContext, route, fileName, waitMs));
  }

  for (const [route, fileName, waitMs] of customerRoutes) {
    captures.push(await capture(customerContext, route, fileName, waitMs));
  }

  for (const [route, fileName, waitMs] of adminRoutes) {
    captures.push(await capture(adminContext, route, fileName, waitMs));
  }

  await guestContext.close();
  await customerContext.close();
  await adminContext.close();
  await browser.close();

  const manifest = {
    createdAt: new Date().toISOString(),
    outputDirectory: OUT_DIR,
    totalScreenshots: captures.length,
    customerDemoCredentials: auth.customerCreds,
    adminLoginVerified: auth.adminLoginOk,
    captures,
  };

  fs.writeFileSync(path.join(OUT_DIR, 'manifest.json'), JSON.stringify(manifest, null, 2));

  console.log(`SCREENSHOT_DIR=${OUT_DIR}`);
  console.log(`SCREENSHOT_COUNT=${captures.length}`);
}

main().catch((error) => {
  console.error('SCREENSHOT_CAPTURE_FAILED');
  console.error(error && error.stack ? error.stack : error);
  process.exit(1);
});
