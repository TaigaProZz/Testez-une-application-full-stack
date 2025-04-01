describe('Register spec', () => {
  it('Register successfull', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
      body: {
        message: "User registered successfully!"
      },
    })

    cy.get('input[formControlName=firstName]').type('a')
    cy.get('input[formControlName=lastName]').type('a')
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.url().should('include', '/login')
  })

  it('FirstName is empty', () => {
    cy.visit('/register')

    cy.get('input[formControlName=firstName]').focus()
    cy.get('input[formControlName=lastName]').type('a')
    cy.get('input[formControlName=email]').type('a@a.a')
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.get('button[type=submit]').should('be.disabled')

    cy.get('input[formControlName=firstName]').should('have.class', 'ng-invalid')
  })

  it('LastName is empty', () => {
    cy.visit('/register')

    cy.get('input[formControlName=firstName]').type('a')
    cy.get('input[formControlName=lastName]').focus()
    cy.get('input[formControlName=email]').type('a@a.a')
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.get('button[type=submit]').should('be.disabled')

    cy.get('input[formControlName=lastName]').should('have.class', 'ng-invalid')
  })

  it('Email is empty', () => {
    cy.visit('/register')

    cy.get('input[formControlName=firstName]').type('a')
    cy.get('input[formControlName=lastName]').type('a')
    cy.get('input[formControlName=email]').focus()
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.get('button[type=submit]').should('be.disabled')

    cy.get('input[formControlName=email]').should('have.class', 'ng-invalid')
  })

  it('Password is empty', () => {
    cy.visit('/register')

    cy.get('input[formControlName=firstName]').type('a')
    cy.get('input[formControlName=lastName]').type('a')
    cy.get('input[formControlName=email]').type('a@a.a')
    cy.get('input[formControlName=password]').focus()

    cy.get('button[type=submit]').should('be.disabled')

    cy.get('input[formControlName=password]').should('have.class', 'ng-invalid')
  })

  it('Error returned', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 401,
      body: {
        message: 'Invalid credentials',
      },
    }).as('registerRequest')


    cy.get('input[formControlName=firstName]').type('a')
    cy.get('input[formControlName=lastName]').type('a')
    cy.get('input[formControlName=email]').type('a@a.a')
    cy.get('input[formControlName=password]').type("a{enter}{enter}")

    cy.get('button[type=submit]').click()
    cy.wait('@registerRequest')

    cy.get('span').should('have.class', 'error')
  })
});
