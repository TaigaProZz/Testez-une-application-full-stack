// cypress/support/commands.js

// create login command

Cypress.Commands.add('login', () => {
  cy.visit('/login');

  cy.intercept('POST', '/api/auth/login', {
    statusCode: 200,
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
    [])

  cy.get('input[formControlName=email]').type("a@a.a");
  cy.get('input[formControlName=password]').type("a");
  cy.get('button[type=submit]').click();

  cy.url().should('include', '/sessions');
});
