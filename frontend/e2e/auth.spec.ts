import { expect, test } from '@playwright/test'

test('permite registrar un usuario y entrar al dashboard', async ({ page }) => {
  const unique = Date.now()
  const email = `playwright.${unique}@sanosysalvos.cl`
  const password = 'demo123456'

  await page.goto('/register')

  await page.getByLabel('Nombre').fill('Usuario')
  await page.getByLabel('Apellido').fill('Playwright')
  await page.getByLabel('Email').fill(email)
  await page.getByLabel('Contraseña', { exact: true }).fill(password)
  await page.getByLabel('Confirmar').fill(password)

  await page.getByRole('button', { name: /crear mi cuenta/i }).click()

  await expect(page).toHaveURL(/\/dashboard/)
  await expect(page.getByRole('heading', { name: /Usuario Playwright/ })).toBeVisible()
  await expect(page.getByRole('link', { name: /crear nuevo reporte/i })).toBeVisible()
})

test('muestra error con credenciales incorrectas', async ({ page }) => {
  await page.goto('/login')

  await page.getByLabel('Email').fill('noexiste@sanosysalvos.cl')
  await page.getByLabel('Contraseña').fill('claveincorrecta')
  await page.getByRole('button', { name: /ingresar/i }).click()

  await expect(page.getByText(/email o contraseña incorrectos/i)).toBeVisible()
})
