describe('Me spec', () => {
  it('try to go on /me page being logged out', () => {
    cy.visit('/me')
    cy.url().should('include', '/login')
  })

  it('check user infos on /me page being logged in as user', () => {
    cy.intercept('GET', '/api/user/1', {
      body: {
        email: 'a@a.a',
        firstName: 'a',
        lastName: 'a',
        admin: false,
        createdAt: new Date(2000, 11, 20),
        updatedAt: new Date(2000, 11, 20)
      }
    })

    cy.login()

    cy.contains('span.link', 'Account').should('be.visible')
    cy.contains('span.link', 'Account').click()

    cy.url().should('include', '/me')

    // check user infos
    cy.contains('p', 'Name: a').should('be.visible');
    cy.contains('p', 'Email: a@a.a').should('be.visible');
    cy.contains('p', 'Delete my account:').should('be.visible');
    cy.contains('p', 'Create at: December 20, 2000').should('be.visible');
    cy.contains('p', 'Last update: December 20, 2000').should('be.visible');

    // button delete
    cy.contains('button', 'Detail').should('be.visible');
    cy.contains('mat-icon', 'delete').should('be.visible');
  })

  it('check user infos on /me page being logged in as admin', () => {
    cy.intercept('GET', '/api/user/1', {
      body: {
        email: 'a@a.a',
        firstName: 'a',
        lastName: 'a',
        admin: true,
        createdAt: new Date(2000, 11, 20),
        updatedAt: new Date(2000, 11, 20)
      }
    })

    cy.login()

    cy.contains('span.link', 'Account').should('be.visible')
    cy.contains('span.link', 'Account').click()

    cy.url().should('include', '/me')

    // check user infos
    cy.contains('p', 'Name: a').should('be.visible');
    cy.contains('p', 'Email: a@a.a').should('be.visible');
    cy.contains('p', 'Create at: December 20, 2000').should('be.visible');
    cy.contains('p', 'Last update: December 20, 2000').should('be.visible');

    // button delete
    cy.contains('p', 'You are admin').should('be.visible');
  })
});
