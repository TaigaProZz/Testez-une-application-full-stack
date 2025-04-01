describe('Login spec', () => {
  it('Login successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })

  it('Login email is empty', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').focus()

    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.get('button[type=submit]').should('be.disabled')

    cy.get('button[type=submit]').should('be.disabled')
    cy.get('p').should('have.class', 'error')
  })

  it('Login password is empty', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("a@g.com")
    cy.get('input[formControlName=password]')
      .focus()
      .type(`{enter}{enter}`)

    cy.get('button[type=submit]').should('be.disabled')
    cy.get('p').should('have.class', 'error')
  })

  it('Login error with invalid credentials', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        message: 'Invalid credentials',
      },
    }).as('loginRequest')

    cy.get('input[formControlName=email]').type("a@a.a")
    cy.get('input[formControlName=password]').type("a{enter}{enter}")

    cy.wait('@loginRequest')

    cy.get('p').should('have.class', 'error')
  })
});
